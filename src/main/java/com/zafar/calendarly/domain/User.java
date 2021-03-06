package com.zafar.calendarly.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.repository.Lock;

/**
 * User Domain class
 *
 * @author Zafar Ansari
 */
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "USER_EMAIL", nullable = false, unique = true)
  private String email;

  @Column(name = "USER_NAME", nullable = false)
  private String name;

  /**
   * password should be hashed with salt
   */
  @Column(name = "HASHED_PASSWORD", nullable = false)
  private char[] hashedPassword;

  /**
   * A unique randomly generated string serves as a salt
   */
  @Column(name = "SALT", nullable = false)
  private String salt;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "slotOwner")
  private List<Slot> slotsOwned;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "slotBooker")
  private List<Slot> slotsBooked;

  public String getName() {
    return name;
  }

  public char[] getHashedPassword() {
    return hashedPassword;
  }

  public String getSalt() {
    return salt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setHashedPassword(char[] hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public User() {
  }

  public User(Integer id) {
    this.id = id;
  }

  public List<Slot> getSlotsOwned() {
    return slotsOwned;
  }

  public List<Slot> getSlotsBooked() {
    return slotsBooked;
  }

  public User(String email, String name, char[] hashedPassword, String salt) {
    this.email = email;
    this.name = name;
    this.hashedPassword = hashedPassword;
    this.salt = salt;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
