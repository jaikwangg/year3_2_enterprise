@Repository
public class UserRepository {
	private List<User> users;
	public UserRepository() {
		users = new ArrayList<>();
		users.add(new User("John", "Henry", "john@gmail.com"));
		users.add(new User("Marry", "Watson", "marry@gmail.com"));
		users.add(new User("Clark", "Kent", "clark@gmail.com"));
	}
	public List<User> getUsers() {
		return users;
	}
	public void save(User user) {
		this.users.add(user);
	}
	public void delete(int userId) {
		this.users.remove(userId);
	}
	public User getUser(int userId) {
		return users.get(userId);
	}
	public void update(int userId, User user) {
		users.set(userId, user);
	}
}
