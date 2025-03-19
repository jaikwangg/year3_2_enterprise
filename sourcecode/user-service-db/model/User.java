@Entity
@JsonInclude(Include.NON_NULL)
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long userId;
	@Column(length=30)
	@JsonProperty("first_name")
	private String firstName;
	@Column(length=30)
	@JsonProperty("last_name")
	private String lastName;
	@Column(length=20)
	private String email;
	@CreationTimestamp
	@JsonIgnore
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss")
	private Date creationDate;
	public User() {
		
	}
	public User(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
