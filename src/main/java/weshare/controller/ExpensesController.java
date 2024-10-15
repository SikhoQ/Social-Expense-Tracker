package weshare.controller;

import io.javalin.http.Handler;
import org.javamoney.moneta.function.MonetaryFunctions;
import org.jetbrains.annotations.NotNull;
import weshare.model.*;
import weshare.persistence.ExpenseDAO;
import weshare.server.Routes;
import weshare.server.ServiceRegistry;
import weshare.server.WeShareServer;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

import static weshare.model.MoneyHelper.ZERO_RANDS;

public class ExpensesController {

    public static final Handler view = context -> {

        // Handler for viewing expenses
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person personLoggedIn = WeShareServer.getPersonLoggedIn(context);

        // Retrieve expenses not fully paid by others
        Collection<Expense> expenses = expensesDAO.findExpensesForPerson(personLoggedIn);
        List<Expense> unpaidExpenses = expenses.stream()
                .filter(expense -> !expense.isFullyPaidByOthers())
                .collect(Collectors.toList());

        // Prepare the view model
        Map<String, Object> viewModel = new HashMap<>(Map.of("expenses", unpaidExpenses));
        MonetaryAmount totalAmount = calculateTotalAmount(unpaidExpenses);

        // Add total to the view model
        viewModel.put("total", totalAmount);
        context.render("expenses.html", viewModel);
    };

    // Handler for rendering the new expense form
    public static Handler newExpense = context -> {
        // Logic to render the new expense form
        context.render("newexpense.html");
    } ;

    // Handler for creating a new expense
    public static final Handler expenseAction = context -> {
        ExpenseDAO expenseDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person person = WeShareServer.getPersonLoggedIn(context);

        // Validate and create expense
        String description = context.formParam("description");
        LocalDate date = LocalDate.parse(context.formParam("date"));
        MonetaryAmount amount = MoneyHelper.amountOf(Long.parseLong(context.formParam("amount")));

        Expense newExpense = new Expense(person, description, amount, date);
        expenseDAO.save(newExpense);
        context.sessionAttribute(WeShareServer.SESSION_USER_KEY, person);
        context.redirect(Routes.EXPENSES);
    };

    // Handler for viewing a specific payment request
    public static final Handler payment_request = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        UUID uuid = UUID.fromString(context.queryParam("expenseId"));
        Optional<Expense> expense = expensesDAO.get(uuid);

        // Render payment request view
        expense.ifPresent(exp -> context.render("paymentrequest.html", Map.of("expense", exp)));
    };

    // Handler for viewing sent payment requests
    public static final Handler payment_request_sent = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person person = WeShareServer.getPersonLoggedIn(context);
        Collection<PaymentRequest> paymentRequests = expensesDAO.findPaymentRequestsSent(person);

        // Prepare view model
        MonetaryAmount total = calculateTotalPaymentRequests(paymentRequests);
        context.render("paymentrequests_sent.html", Map.of("newexpenses", paymentRequests, "total", total));
    };

    // Handler for submitting a payment request
    public static final Handler payment_request_Action = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        UUID expenseId = UUID.fromString(context.formParam("expense_id")); // Assuming "expense_id" is being sent in the form
        Optional<Expense> optionalExpense = expensesDAO.get(expenseId);

        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            Person person = new Person(context.formParam("email"));
            LocalDate dueDate = LocalDate.parse(context.formParam("due_date"), DateHelper.DD_MM_YYYY);

            MonetaryAmount amount = MoneyHelper.amountOf(Long.parseLong(context.formParam("amount")));
            expense.requestPayment(person, amount, dueDate);
            expensesDAO.save(expense);

            // Redirect to the payment request page, passing the expenseId as a query parameter
            context.redirect("/paymentrequest?expenseId=" + expenseId.toString());
        } else {
            context.status(404).result("Expense not found.");
        }
    };


    // Handler for processing received payment requests
    public static final Handler payment_request_Received_Action = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        UUID paymentRequestId = UUID.fromString(context.formParam("payment_request_id"));
        Person person = WeShareServer.getPersonLoggedIn(context);

        // Process payment request
        expensesDAO.findPaymentRequestsReceived(person)
                .stream()
                .filter(pr -> pr.getId().equals(paymentRequestId))
                .findFirst()
                .ifPresent(pr -> {
                    pr.pay(person, DateHelper.TODAY);
                    Expense newExpense = new Expense(person, pr.getDescription(), pr.getAmountToPay(), DateHelper.TODAY);
                    expensesDAO.save(newExpense);
                    context.redirect(Routes.PAYMENT_REQUEST_RECEIVED_SENT);
                });
    };

    // Handler for viewing received payment requests
    public static final Handler payment_request_Received_sent = context -> {
        ExpenseDAO expensesDAO = ServiceRegistry.lookup(ExpenseDAO.class);
        Person person = WeShareServer.getPersonLoggedIn(context);
        Collection<PaymentRequest> paymentRequests = expensesDAO.findPaymentRequestsReceived(person);

        // Prepare view model
        MonetaryAmount total = paymentRequests.stream()
                .filter(pr -> !pr.isPaid())
                .map(PaymentRequest::getAmountToPay)
                .reduce(ZERO_RANDS, MonetaryAmount::add);

        context.render("paymentrequests_received.html", Map.of("newexpenses", paymentRequests, "total", total));
    };

    // Helper method to calculate total amount from expenses
    private static MonetaryAmount calculateTotalAmount(List<Expense> expenses) {
        return expenses.stream()
                .map(expense -> expense.getAmount().subtract(expense.totalAmountForPaymentsReceived()))
                .reduce(ZERO_RANDS, MonetaryAmount::add);
    }

    // Helper method to calculate total amount from payment requests
    private static MonetaryAmount calculateTotalPaymentRequests(Collection<PaymentRequest> paymentRequests) {
        return paymentRequests.stream()
                .map(PaymentRequest::getAmountToPay)
                .reduce(ZERO_RANDS, MonetaryAmount::add);
    }
}
