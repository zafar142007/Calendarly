package com.zafar.calendarly.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 * User Domain class
 *
 * @author Zafar Ansari
 */
public class User {

  @Id
  @Column("ID")
  private Integer id;

  @Column("USER_EMAIL")
  private String email;

  @Column("USER_NAME")
  private String name;

  /**
   * password should be hashed with salt
   */
  @Column("HASHED_PASSWORD")
  private String hashedPassword;

  /**
   * A unique randomly generated string serves as a salt
   */
  @Column("SALT")
  private String salt;

  public String getName() {
    return name;
  }

  public String getHashedPassword() {
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

  public void setHashedPassword(String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public User() {
  }

  public User(Integer id, String email, String name, String hashedPassword, String salt) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.hashedPassword = hashedPassword;
    this.salt = salt;
  }

  public User(Integer id) {
    this.id = id;
  }

  public User(String email, String name, String hashedPassword, String salt) {
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
