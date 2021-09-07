package serviceServer;

import data.*;
import dataNet.Command;
import dataNet.Runner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import psql.PSQLWorker;

public class RunnerParserServer implements Runnable {
    
    private MemoryDB memoryDB;
    private Runner runner;
    private PSQLWorker pSQLWorker;
    private static org.slf4j.Logger logger;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    public RunnerParserServer(Socket socket, MemoryDB memoryDB, PSQLWorker pSQLWorker) {
        this.socket = socket;
        this.memoryDB = memoryDB;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            runner = (Runner) ois.readObject();
        } catch (Exception ex) {
        }
        this.pSQLWorker = pSQLWorker;
        logger = LoggerFactory.getLogger(PSQLServer.class);
    }

    /**
     * Разбор объекта
     */
    public void run() {
        //получение команды
        Command com = runner.getCom();
        logger.info("Обработка запроса клиента: " + com);
        switch (com) {
            case INFO:
                info();
                break;
            case SHOW:
                show();
                break;
            case ADD:
                addIf(null, Command.ADD);
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
                addUser();
                break;
            case UPDATE_USER_PASSWORD:
                updateUserPassword();
                break;
            case DELETE_USER:
                deleteUser();
                break;
            case SHOW_USERS:
                showUsers();
                break;
        }
    }

    /**
     * Отправка данных клиенту
     *
     * @param r объект для отправки
     */
    private void writeObject(Runner r) {
        try ( Socket s = socket;  ObjectInputStream is = ois;  ObjectOutputStream os = oos) {
            os.writeObject(r);
            logger.info("Обработка соединения окончена");
        } catch (Exception ex) {
            logger.error("Ошибка соединения с клиентом");
        }
    }

    /**
     * Хэширует входную строку
     *
     * @param password сходная строку
     * @return хэш строка
     */
    private String MD5(String password) {
        MessageDigest messageDigest = null;
        byte[] digest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        BigInteger bigInt = new BigInteger(digest);
        String md5Hex = bigInt.toString(16);
        while (md5Hex.length() < 32)
            md5Hex = "0" + md5Hex;
        return md5Hex;
    }

    /**
     * Ищет пользователя в базе данных
     *
     * @param user пользователь для поиска
     * @return найденный пользователь
     */
    private User checkUser(User user) {
        return memoryDB.getUsers().stream().filter(us -> us.getUserName().equals(user.getUserName())
                && us.getPassword().equals(MD5(user.getPassword()))).findFirst().orElse(null);
    }

    /**
     * Ищет организацию в базе данных
     *
     * @param organization для поиска
     * @return найденная организация
     */
    private Organization checkOrganization(Organization organization) {
        return memoryDB.getOrganizations().stream().filter(org -> org.getID() == organization.getID()).findFirst().orElse(null);
    }

    /**
     * Добавление нового пользователя
     */
    private void addUser() {
        User user = (User) runner.getObject();
        String text;
        if (Objects.isNull(checkUser(user))) {
            user.setID(pSQLWorker.getUsersNextID(memoryDB.getUsers()));
            user.setPassword(MD5(user.getPassword()));
            if (pSQLWorker.addOrUpdate(Collections.singletonList(user), null, true)) {
                memoryDB.SAddUser(user);
                text = "Новый пользователь успешно добавлен";
            } else text = "Ошибка базы данных. Пользователь не добавлен.";
        } else text = "Этот пользователь уже есть в системе";
        Runner r = new Runner(Command.ADD_USER, text);
        writeObject(r);
    }

    /**
     * Изменение пароль пользователя
     */
    private void updateUserPassword() {
        User newUser = (User) runner.getObject();
        String text;
        User oldUser = checkUser(newUser);
        if (Objects.nonNull(oldUser)) {
            newUser.setID(oldUser.getID());
            newUser.setPassword(newUser.getNewPassword());
            if (pSQLWorker.addOrUpdate(Collections.singletonList(newUser), null, false)) {
                memoryDB.SRemoveUser(oldUser);
                memoryDB.SAddUser(newUser);
                text = "Пароль пользователя " + newUser.getUserName() + " успешно изменен.";
            } else text = "Ошибка базы данных. Пароль не изменен.";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.UPDATE_USER_PASSWORD, text);
        writeObject(r);
    }

    /**
     * Удаление пользователя
     */
    private void deleteUser() {
        User user = checkUser((User) runner.getObject());
        String text;
        if (Objects.nonNull(user)) {
            if (pSQLWorker.delete(Collections.singletonList(user), null)) {
                memoryDB.SRemoveUser(user);
                text = "Пользователь " + user.getUserName() + " успешно удален.";
            } else text = "Ошибка базы данных. Пользователь не удален.";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.DELETE_USER, text);
        writeObject(r);
    }

    /**
     * Вывод всех пользователей
     */
    private void showUsers() {
        User user = checkUser((User) runner.getObject());
        Object text = "";
        List<User> usersList = new ArrayList<>();
        if (Objects.nonNull(user)) {
            if (user.getID() == 0) {
                usersList = memoryDB.getUsers();
                text = "Список всех пользователей успешно сформирован.";
            } else
                text = "Вы не являетесь администратором. Операция запрещена.";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.SHOW_USERS, new Object[]{text, usersList});
        writeObject(r);
    }

    /**
     * Вывод информации о коллекции
     */
    private void info() {
        User user = checkUser((User) runner.getObject());
        String text;
        if (Objects.nonNull(user)) {
            text = "Тип коллекции: " + memoryDB.getOrganizations().getClass().getName() + "\n"
                    + "Количество элементов в коллекции: " + memoryDB.getOrganizations().size();
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.INFO, text);
        writeObject(r);
    }

    /**
     * Вывод всех элементов коллекции
     */
    private void show() {
        User user = checkUser((User) runner.getObject());
        String text = "";
        Set<Organization> org = new LinkedHashSet<>();
        if (Objects.nonNull(user)) {
            org = memoryDB.getOrganizations();
            text = "Операция выполнена успешно.";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.SHOW, new Object[]{text, org});
        writeObject(r);
    }

    /**
     * Добавление нового элемента в коллекцию
     */
    private void addIf(Function<Organization, String> predicate, Command command) {
        Object[] args = (Object[]) runner.getObject();
        String text = null;
        User user = checkUser((User) args[0]);
        if (Objects.nonNull(user)) {
            Organization organization = (Organization) args[1];
            organization.setId(pSQLWorker.getCollNextID(memoryDB.getOrganizations()));
            organization.setUserID(user.getID());
            if (Objects.nonNull(predicate))
                text = predicate.apply(organization);
            if (Objects.isNull(text)) {
                if (pSQLWorker.addOrUpdate(null, Collections.singleton(organization), true)) {
                    text = organization + " успешно добавлена в базу данных";
                    memoryDB.SAddOrganization(organization);
                } else
                    text = "Ошибка базы данных. Не удалось добавить организацию " + organization;
            }
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(command, text);
        writeObject(r);
    }

    /**
     * Обновление значение элемента коллекции по его id
     *
     */
    private void update() {
        Object[] args = (Object[]) runner.getObject();
        String text;
        User user = checkUser((User) args[0]);
        if (Objects.nonNull(user)) {
            Organization organization = (Organization) args[1];
            Organization existOrganization = checkOrganization(organization);
            if (Objects.nonNull(existOrganization)) {
                if (user.getID() == existOrganization.getUserID()) {
                    organization.setUserID(user.getID());
                    if (pSQLWorker.addOrUpdate(null, Collections.singleton(organization), false)) {
                        text = organization + " успешно изменена в базе данных";
                        memoryDB.SRemoveOrganization(existOrganization);
                        memoryDB.SAddOrganization(organization);
                    } else
                        text = "Ошибка базы данных. Не удалось добавить организацию " + organization;
                } else
                    text = "Организация создана другим пользователем. Операция запрещена.";
            } else
                text = "Изменяемая организация не найдена";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.UPDATE_BY_ID, text);
        writeObject(r);
    }

    /**
     * Удаление элемента коллекции по его id
     *
     */
    private void removeById() {
        Object[] args = (Object[]) runner.getObject();
        User user = checkUser((User) args[0]);
        Long id = (Long) args[1];
        String text;
        if (Objects.nonNull(user)) {
            Organization organization = checkOrganization(new Organization(id));
            if (organization.getUserID() == user.getID()) {
                if (Objects.nonNull(organization)) {
                    if (pSQLWorker.delete(null, Collections.singleton(organization))) {
                        memoryDB.SRemoveOrganization(organization);
                        text = "Организация с id = " + id + " успешно удалена из базы данных";
                    } else
                        text = "Ошибка базы данных. Не удалось удалить организацию с id = " + id;
                } else
                    text = "Организация с id = " + id + " не найдена в базе данных.";
            } else
                text = "Организация создана другим пользователем. Операция запрещена.";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.REMOVE_BY_ID, text);
        writeObject(r);
    }

    /**
     * Очистка коллекции
     */
    private void clear() {
        Object[] args = (Object[]) runner.getObject();
        User user = checkUser((User) args[0]);
        String text;
        if (user.getID() == 0) {
            if (pSQLWorker.allDelete(true)) {
                memoryDB.getOrganizations().clear();
                text = "Коллекция очищена.";
            } else
                text = "Ошибка базы данных. Не удалось очистить базу данных коллекций.";
        } else text = "Вы не являетесь администратором. Операция запрещена.";
        Runner r = new Runner(Command.CLEAR, text);
        writeObject(r);
    }

    /**
     * Добавление новой организации, если его значение капитала больше
     * максимального значения капитала огранизаций в коллекции
     */
    private void addIfMax() {
        addIf(org -> {
            Organization organizationMax = memoryDB.getOrganizations().stream().max((Organization o1, Organization o2) -> {
                return Double.compare(o1.getAnnualTurnover(), o2.getAnnualTurnover());
            }).get();
            if (org.getAnnualTurnover() > organizationMax.getAnnualTurnover())
                return null;
            else
                return "Ошибка. Организация имеет недостаточно капиталла.";
        }, Command.ADD_IF_MAX);
    }

    /**
     * Добавление новой организации, если его значение капитала меньше
     * максимального значения капитала огранизаций в коллекции
     */
    private void addIfMin() {
        addIf(org -> {
            Organization organizationMax = memoryDB.getOrganizations().stream().max((Organization o1, Organization o2) -> {
                return Double.compare(o1.getAnnualTurnover(), o2.getAnnualTurnover());
            }).get();
            if (org.getAnnualTurnover() < organizationMax.getAnnualTurnover())
                return null;
            else
                return "Ошибка. Организация имеет слишком много капиталла.";
        }, Command.ADD_IF_MIN);
    }

    /**
     * Вывод элементов коллекции по заданному типу
     *
     */
    private void filterByType() {
        Object[] args = (Object[]) runner.getObject();
        User user = checkUser((User) args[0]);
        String text;
        Set<Organization> setCollect = new LinkedHashSet<>();
        if (Objects.nonNull(user)) {
            OrganizationType type = (OrganizationType) args[1];
            setCollect = memoryDB.getOrganizations().stream().filter((Organization o) -> {
                return o.getType() == type;
            }).collect(Collectors.toSet());
            text = "Операция выполнена успешно";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.FILTER_BY_TYPE, new Object[]{text, setCollect});
        writeObject(r);
    }

    /**
     * Вывод элементов коллекции, значение поля name начинаются с заданной
     * подстроки
     *
     */
    private void filterStartsWithName() {
        Object[] args = (Object[]) runner.getObject();
        User user = checkUser((User) args[0]);
        String text;
        Set<Organization> setCollect = new LinkedHashSet<>();
        if (Objects.nonNull(user)) {
            String start = (String) args[1];
            setCollect = memoryDB.getOrganizations().stream().filter((Organization o) -> {
                return o.getName().startsWith(start);
            }).collect(Collectors.toSet());
            text = "Операция выполнена успешно";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.FILTER_STARTS_WITH_NAME, new Object[]{text, setCollect});
        writeObject(r);
    }

    /**
     * Вывод элементов коллекции, значение поля address больше заданного
     * (сравниваем по zipCode)
     *
     */
    private void filterGreaterThanPostalAddress() {
        Object[] args = (Object[]) runner.getObject();
        User user = checkUser((User) args[0]);
        String text;
        Set<Organization> setCollect = new LinkedHashSet<>();
        if (Objects.nonNull(user)) {
            Address address = (Address) args[1];
            setCollect = memoryDB.getOrganizations().stream().filter((Organization o) -> {
                if (o.getPostalAddress() == null) {
                    return false;
                }
                return o.getPostalAddress().compareTo(address) > 0;
            }).collect(Collectors.toSet());
            text = "Операция выполнена успешно";
        } else
            text = "Вы не являетесь авторизованным пользователем. Операция запрещена.";
        Runner r = new Runner(Command.FILTER_GREATER_THAN_POSTAL_ADDRESS, new Object[]{text, setCollect});
        writeObject(r);
    }
}
