package org.inftel.ssa.domain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ibaca
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
  @PersistenceContext(unitName = "ssa-persistence-unit")
  private EntityManager em;

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public UserFacade() {
    super(User.class);
  }
  
  public User findByEmail() {
	  return getEntityManager().createNamedQuery("User.findByEmail", User.class).getSingleResult();
  }
  
  // internal test usage
  UserFacade(EntityManager em) {
	  this();
	  this.em = em;
  }
  
}
