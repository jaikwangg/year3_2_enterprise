@Repository
public interface UserRepository extends JpaRepository <User, Long>{
	User findByUserId(long id);
	void deleteByUserId(long id);
	User findByFirstName(String firstName);
	/*private List<User> users;
	public UserRepository() {
		this.users = new ArrayList<>();
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
	public void update(int userId, User user) {
		this.users.set(userId, user);
	}
	public void delete(int userId) {
		this.users.remove(userId);
		
	}*/
}
