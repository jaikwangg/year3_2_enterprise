@Service
@Transactional
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository repository;
	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public User getUser(long userId) {
		return repository.findByUserId(userId);
	}

	@Override
	public void create(User user) {
		repository.save(user);
		
	}
	@Override
	public void update(User user, PatchUserRequest request) {
		updateUser(user, request);
		repository.save(user);
	}
	public void updateUser(User user, PatchUserRequest request) {
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getEmail() != null) {
			user.setEmail(request.getEmail());
		}
		
	}
	@Override
	public void delete(long userId) {
		repository.deleteByUserId(userId);
		
	}

	@Override
	public User getUserByFirstName(String firstName) {
		// TODO Auto-generated method stub
		return repository.findByFirstName(firstName);
	}

}
