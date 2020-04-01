package com.zafar.calendarly.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User Domain class
 * @author Zafar Ansari
 */
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  /**
   * password should be hashed with salt
   */
  @Column(nullable = false)
  private char[] hashedPassword;

  /**
   * A unique randomly generated string serves as a salt
   */
  @Column(nullable = false)
  private String salt;

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
}
