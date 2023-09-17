package pl.javastart.streamstask;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ExampleTest {
    List<User> users = TestData.getUsers();
    List<Expense> expenses = TestData.getExpenses();
    private StreamsTask streamsTask = new StreamsTask();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    public void shouldWomenHasSize2() {
        Collection<User> women = streamsTask.findWomen(users);
        softAssertions.assertThat(women)
                .as("Women size")
                .hasSize(2);
    }

    @Test
    public void shouldWomenHasSize0WhenEmptyList() {
        Collection<User> women = streamsTask.findWomen(new ArrayList<>());
        assertThat(women).isEmpty();
    }

    @Test
    public void shouldWomenHasNames() {
        Collection<User> women = streamsTask.findWomen(users);
        softAssertions.assertThat(women)
                .as("Women names")
                .extracting(User::getName)
                .containsExactlyInAnyOrder("Alicja", "Patrycja");
    }

    @Test
    public void shouldWomenNamesHasEndsWithA() {
        Collection<User> women = streamsTask.findWomen(users);
        softAssertions.assertThat(women)
                .as("Women names")
                .allMatch(user -> user.getName().endsWith("a"));
        softAssertions.assertAll();
    }

    @Test
    public void testAverageMenAge() {
        Double averageMenAge = streamsTask.averageMenAge(users);
        assertThat(averageMenAge).isEqualTo(22.25);
    }

    @Test
    public void shouldAverageMenAge0ForEmptyCollection() {
        Double averageMenAge = streamsTask.averageMenAge(new ArrayList<>());
        assertThat(averageMenAge).isEqualTo(0.0);
    }

    @Test
    public void shouldGroupExpensesByUserIdReturnEmptyMapWhenUsersEmpty() {
        Map<Long, List<Expense>> expensesByUserId = streamsTask.groupExpensesByUserId(new ArrayList<>(), expenses);
        assertThat(expensesByUserId).isEmpty();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenUsersNull() {
        assertThatThrownBy(() -> streamsTask.groupExpensesByUserId(null, expenses))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testGroupExpensesByUserId() {
        Map<Long, List<Expense>> expensesByUserId = streamsTask.groupExpensesByUserId(users, expenses);
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
    public void testGroupExpensesByUser() {
        Map<User, List<Expense>> expensesByUser = streamsTask.groupExpensesByUser(users, expenses);
        softAssertions.assertThat(expensesByUser.get(users.get(0))).hasSize(2);
        softAssertions.assertThat(expensesByUser.get(users.get(1))).hasSize(3);
        softAssertions.assertThat(expensesByUser.get(users.get(2))).isEmpty();
        softAssertions.assertThat(expensesByUser.get(users.get(3))).isEmpty();
        softAssertions.assertThat(expensesByUser.get(users.get(4))).isEmpty();
        softAssertions.assertThat(expensesByUser.get(users.get(5))).isEmpty();
        softAssertions.assertAll();
    }
}
