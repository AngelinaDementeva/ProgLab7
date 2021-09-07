package serviceClient;

import data.Organization;
import data.User;
import dataNet.Command;
import dataNet.Runner;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class RunnerParserClient {

    private Runner runner;

    public RunnerParserClient() {

    }

    /**
     * Разбор объекта
     *
     * @param run объект
     */
    public void parse(Runner run) {
        runner = run;
        //получение команды
        Command com = runner.getCom();
        System.out.println("CLIENT---switch: " + com);

        switch (com) {
            case INFO:
                info();
                break;
            case SHOW:
                show();
                break;
            case ADD:
                add();
                break;
            case UPDATE_BY_ID:
                update();
                break;
            case REMOVE_BY_ID:
                removeById();
                break;
            case CLEAR:
                clear();
                break;
            case ADD_IF_MAX:
                addIfMax();
                break;
            case ADD_IF_MIN:
                addIfMin();
                break;
            case FILTER_BY_TYPE:
                filterByType();
                break;
            case FILTER_STARTS_WITH_NAME:
                filterStartsWithName();
                break;
            case FILTER_GREATER_THAN_POSTAL_ADDRESS:
                filterGreaterThanPostalAddress();
                break;
            case ADD_USER:
            case UPDATE_USER_PASSWORD:
            case DELETE_USER:
                processUser();
                break;
            case SHOW_USERS:
                showUsers();
                break;
        }
    }

    /**
     * Установка объекта для передачи по сета
     *
     * @param runner объект
     */
    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    private void processUser() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    private void showUsers() {
        System.out.println("Список всех пользователей:");
        Object[] args = (Object[]) runner.getObject();
        for (User user : (List<User>) args[1])
            System.out.println(user);
        System.out.println((String) args[0]);
    }

    /**
     * Вывод информации о коллекции
     */
    private void info() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    /**
     * Вывод всех элементов коллекции
     */
    private void show() {
        Object[] args = (Object[]) runner.getObject();
        Set<Organization> set = (Set<Organization>) args[1];
        if (set.isEmpty()) {
            System.out.println("Коллекция пустая!!!");
            return;
        }
        System.out.println("Список организаций в коллекции:");
        for (Organization organization : set) {
            System.out.println(organization);
        }
        System.out.println((String) args[0]);
    }

    /**
     * Добавление нового элемента в коллекцию
     */
    private void add() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    /**
     * Обновление значение элемента коллекции
     *
     */
    private void update() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    /**
     * Удаление элемента коллекции
     *
     */
    private void removeById() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    /**
     * Очистка коллекции
     */
    private void clear() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    /**
     * Завершение работы программы
     */
    private void exit() {
        System.exit(0);
    }

    /**
     * Добавление новой организации, если его значение капитала больше
     * максимального значения капитала огранизаций в коллекции
     */
    private void addIfMax() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    /**
     * Добавление новой организации, если его значение капитала меньше
     * максимального значения капитала огранизаций в коллекции
     */
    private void addIfMin() {
        String text = (String) runner.getObject();
        System.out.println(text);
    }

    /**
     * Вывод элементов коллекции по заданному типу
     *
     */
    private void filterByType() {
        Object[] args = (Object[]) runner.getObject();
        Set<Organization> set = (Set<Organization>) args[1];
        if (set.isEmpty()) {
            System.out.println("Организации с заданным типом отсутствуют!!!");
            return;
        }
        System.out.println("Список организаций с заданным типом:");
        for (Organization organization : set) {
            System.out.println(organization);
        }
        System.out.println((String) args[0]);
    }

    /**
     * Вывод элементов коллекции, значение поля name начинаются с заданной
     * подстроки
     *
     */
    private void filterStartsWithName() {
        Object[] args = (Object[]) runner.getObject();
        Set<Organization> set = (Set<Organization>) args[1];
        if (set.isEmpty()) {
            System.out.println("Организации с заданным начаным значением названия отсутствуют!!!");
            return;
        }
        System.out.println("Список организаций с заданным начальным значение названий:");
        for (Organization organization : set) {
            System.out.println(organization);
        }
        System.out.println((String) args[0]);
    }

    /**
     * Вывод элементов коллекции, значение поля address больше заданного
     * (сравниваем по zipCode)
     */
    private void filterGreaterThanPostalAddress() {
        Object[] args = (Object[]) runner.getObject();
        Set<Organization> set = (Set<Organization>) args[1];
        if (set.isEmpty()) {
            System.out.println("Организации с адресом больше заданного отсутствуют!!!");
            return;
        }
        System.out.println("Список организаций с адресом больше заданного:");
        for (Organization organization : set) {
            System.out.println(organization);
        }
        System.out.println((String) args[0]);
    }
}
