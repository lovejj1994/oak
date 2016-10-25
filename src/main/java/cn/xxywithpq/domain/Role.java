package cn.xxywithpq.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;

/**
 * 角色类
 * 
 * @author panqian
 * @date 2016年10月27日 上午9:22:59
 */
@Entity
@Table(name = "OAK_ROLES")
public class Role implements Serializable {

	private static final long serialVersionUID = -159941288176690938L;

	@Id
	@GeneratedValue
	public Long id;

	@NotNull
	@Column(name = "ROLE_NAME", unique = true, length = 50)
	public String roleName;

	@Column(name = "USER")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, targetEntity = cn.xxywithpq.domain.User.class, mappedBy = "roles")
	private List<User> users = Lists.newArrayList();

	@Column(name = "AUTHORITIE")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.EAGER, targetEntity = cn.xxywithpq.domain.Authority.class)
	private List<Authority> authorities = Lists.newArrayList();

	public Role() {
		super();
	}

	public Role(String roleName, List<User> users) {
		super();
		this.roleName = roleName;
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

}
