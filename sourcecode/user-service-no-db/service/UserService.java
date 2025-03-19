public interface UserService {
	public List<User> getUsers();
	public User getUser(String userId);
	public void create(User user);
	public void delete(String userId);
	public void update(String userId, PatchUserRequest request);
}
