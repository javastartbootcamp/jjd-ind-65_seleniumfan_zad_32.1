package pl.javastart.streamstask;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ExampleTest {
    private List<User> users;
    private List<Expense> expenses;
    private StreamsTask streamsTask = new StreamsTask();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Before
    public void setUp() {
        users = new ArrayList<>();
        users.add(new User(1L, "Alicja", 20));
        users.add(new User(2L, "Dominik", 15));
        users.add(new User(3L, "Patrycja", 25));
        users.add(new User(4L, "Marcin", 30));
        users.add(new User(5L, "Tomek", 18));
        users.add(new User(6L, "Damian", 26));

        expenses = new ArrayList<>();
        expenses.add(new Expense(1L, "Buty", new BigDecimal("149.99"), ExpenseType.WEAR));
        expenses.add(new Expense(1L, "Sałatka", new BigDecimal("14.99"), ExpenseType.FOOD));
        expenses.add(new Expense(2L, "Bluza", new BigDecimal("100"), ExpenseType.WEAR));
        expenses.add(new Expense(2L, "Skarpetki", new BigDecimal("39"), ExpenseType.WEAR));
        expenses.add(new Expense(2L, "Pizza", new BigDecimal("25"), ExpenseType.FOOD));
    }

    @Test
    public void shouldFind2WomenByName() {
        Collection<User> women = streamsTask.findWomen(users);
        softAssertions.assertThat(women)
                .as("Women size")
                .hasSize(2);

        softAssertions.assertThat(women)
                .as("Women names")
                .extracting(User::getName)
                .containsExactlyInAnyOrder("Alicja", "Patrycja");
        softAssertions.assertAll();
    }

    @Test
    public void shouldWomenHasSize0WhenEmptyList() {
        Collection<User> women = streamsTask.findWomen(new ArrayList<>());
        assertThat(women).isEmpty();
    }

    @Test
    public void shouldAverageMenAgeNot0WhenCollectionNotEmpty() {
        Double averageMenAge = streamsTask.averageMenAge(users);
        assertThat(averageMenAge).isEqualTo(22.25);
    }

    @Test
    public void shouldAverageMenAge0ForEmptyCollection() {
        Double averageMenAge = streamsTask.averageMenAge(new ArrayList<>());
        assertThat(averageMenAge).isEqualTo(0.0);
    }

    @Test
    public void shouldReturnEmptyExpensesCollectionWhenUsersDontMatchExpenses() {
        Map<Long, List<Expense>> expensesByUserId = streamsTask.groupExpensesByUserId(new ArrayList<>(), expenses);
        assertThat(expensesByUserId).isEmpty();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGroupingExpensesWithNullUsers() {
        assertThatThrownBy(() -> streamsTask.groupExpensesByUserId(null, expenses))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldGroupExpensesByUserId() {
        Map<Long, List<Expense>> expensesByUserId = streamsTask.groupExpensesByUserId(users, expenses);
        softAssertions.assertThat(expensesByUserId).hasSize(6);
        softAssertions.assertThat(expensesByUserId.get(1L)).hasSize(2);
        softAssertions.assertThat(expensesByUserId.get(2L)).hasSize(3);
        softAssertions.assertThat(expensesByUserId)
                .allSatisfy((key, value) -> {
                    if (key == 3L || key == 4L || key == 5L || key == 6L) {
                        assertThat(value).isEmpty();
                    }
                });
        softAssertions.assertAll();
    }

    @Test
    public void shouldGroupExpensesByUser() {
        Map<User, List<Expense>> expensesByUser = streamsTask.groupExpensesByUser(users, expenses);
        softAssertions.assertThat(expensesByUser).hasSize(6);
        softAssertions.assertThat(expensesByUser.get(users.get(0))).hasSize(2);
        softAssertions.assertThat(expensesByUser.get(users.get(1))).hasSize(3);
        softAssertions.assertThat(expensesByUser)
                .allSatisfy((user, value) -> {
                    if (user.getId() == 3L || user.getId() == 4L || user.getId() == 5L || user.getId() == 6L) {
                        assertThat(value).isEmpty();
                    }
                });
        softAssertions.assertAll();
    }
}
