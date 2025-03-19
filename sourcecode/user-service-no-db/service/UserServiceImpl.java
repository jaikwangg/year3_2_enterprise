@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repository;
	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return repository.getUsers();
	}

	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		return repository.getUser(Integer.valueOf(userId));
	}

	@Override
	public void create(User user) {
		// TODO Auto-generated method stub
		repository.save(user);

	}

	@Override
	public void delete(String userId) {
		// TODO Auto-generated method stub
		repository.delete(Integer.valueOf(userId));

	}

	@Override
	public void update(String userId, PatchUserRequest request) {
		// TODO Auto-generated method stub
		User user = getUser(userId);
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getEmail() != null) {
			user.setEmail(request.getEmail());
		}
		repository.update(Integer.valueOf(userId), user);
	}

}
