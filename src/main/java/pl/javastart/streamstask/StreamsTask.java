package pl.javastart.streamstask;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamsTask {
    List<User> users = TestData.getUsers();
    List<Expense> expenses = TestData.getExpenses();

    public static void main(String[] args) {
        StreamsTask streamsTask = new StreamsTask();
        streamsTask.run();
    }

    private void run() {
        Collection<User> women = findWomen(users);
        Double averageMenAge = averageMenAge(users);
        Map<Long, List<Expense>> expensesByUserId = groupExpensesByUserId(users, expenses);
        Map<User, List<Expense>> expensesByUser = groupExpensesByUser(users, expenses);
    }

    // metoda powinna zwracać listę kobiet (sprawdzając, czy imię kończy się na "a")
    Collection<User> findWomen(Collection<User> users) {
        return users.stream()
                .filter(this::checkSex)
                .collect(Collectors.toList());
    }

    // metoda powinna zwracać średni wiek mężczyzn (sprawdzając, czy imię nie kończy się na "a")
    Double averageMenAge(Collection<User> users) {
        return users.stream()
                .filter(user -> !checkSex(user)) //check sex
                .mapToDouble(User::getAge)
                .average()
                .orElse(0.0);
    }

    private boolean checkSex(User user) {
        return user.getName().endsWith("a");
    }

    // metoda powinna zwracać wydatki zgrupowane po ID użytkownika
    Map<Long, List<Expense>> groupExpensesByUserId(Collection<User> users, List<Expense> expenses) {
        return users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> expenses.stream()
                                .filter(expense -> expense.getUserId().equals(user.getId()))
                                .collect(Collectors.toList())
                ));
    }

    // metoda powinna zwracać wydatki zgrupowane po użytkowniku
    // podobne do poprzedniego, ale trochę trudniejsze
    Map<User, List<Expense>> groupExpensesByUser(Collection<User> users, List<Expense> expenses) {
        return users.stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> expenses.stream()
                                .filter(expense -> expense.getUserId().equals(user.getId()))
                                .collect(Collectors.toList())
                ));
    }
}
