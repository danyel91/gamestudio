package sk.tuke.gamestudio.server.service.serviceJPA;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

@Transactional
public class ScoreServiceJPA implements ScoreService{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addScore(Score score) throws ScoreException {
		entityManager.persist(score);
		
	}

	@Override
	public List<Score> getBestScores(String game) throws ScoreException {
		// TODO Auto-generated method stub
//		return entityManager.createQuery("select s from Score s where s.game = :game")
//				.setParameter("game", game)
//				.getResultList();
		
		return entityManager.createNamedQuery("Score.selectScore")
				.setParameter("game", game)
				.getResultList();
	}

}
