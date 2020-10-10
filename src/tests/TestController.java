package tests;

import constants.EnemyType;
import constants.TowerType;
import controller.TowerDefenseController;
import exceptions.InvalidPlacementException;
import messages.*;
import models.Enemy;
import models.Level;
import models.Position;
import models.Tower;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import constants.Constants;
import constants.Stage;

import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

public class TestController {

	Observer obs = new Observer() {
		@Override
		public void update(Observable o, Object arg) {

		}
	};
	TowerDefenseController controller = new TowerDefenseController(obs);

	@Test
	public void Test_Initialize_Stage() {
		controller.runGame();
		assertTrue(controller.initialize("STAGE1", obs));
		assertEquals(controller.getObserver(), obs);
		assertEquals(controller.getBase().getCol(), 5);
		assertEquals(controller.getBase().getRow(), 4);
		assertEquals(controller.getCurrency(), 1500);
		assertFalse(controller.initialize("STAGE1", obs));
		assertTrue(controller.setPaused(true));
		assertEquals(controller.getCurrency(), 1500);
		assertEquals(controller.getHealth(), 100);
		assertEquals(controller.getEnemyPath().size(), 19);
	}

	@Test
	public void Test_Initialize_Enemy() throws InvalidPlacementException {
		controller.runGame();
		assertTrue(controller.initialize("STAGE1", obs));
		controller.setEnemyStartPoint(5, 5);
		assertEquals(controller.getEnemySize(), 69);
		assertEquals(controller.getBase().getRow(), 4);
		assertFalse(controller.initialize("STAGE1", obs));
	}

	@Test
	public void Test_Tower() throws InvalidPlacementException {
		controller.runGame();
		assertTrue(controller.initialize("STAGE1", obs));
		assertTrue(controller.placeTower(1, 1, Constants.TOWER1));
		controller.placeTower(0, 2, Constants.TOWER3);
		assertEquals(Constants.TOWER1, controller.getTower(1, 1));
		controller.sellTower(1, 1);
		assertNotEquals(Constants.TOWER1, controller.getTower(1, 1));
	}

	@Test
	public void Test_Exceptions() {
		controller.runGame();
		assertTrue(controller.initialize("STAGE1", obs));
		controller.setTickRate(70);
//		controller.tickTransition();
		assertThrows(InvalidPlacementException.class, () -> {
			controller.placeTower(1, 0, Constants.TOWER2);
		});
	}

	@Test
	public void Test_Level() {
		controller.runGame();
		controller.initialize("STAGE1", obs);
		controller.getPosition(0,0);
		controller.getStage();
		controller.startNextLevel();
		controller.restartLevel();
		assertEquals(controller.getBase().getRow(), 7);
		assertEquals(controller.getBase().getCol(), 0);
		controller.restartLevel("STAGE2");
		assertEquals(controller.getBase().getRow(), 7);
		assertEquals(controller.getBase().getCol(), 0);
	}
	@Test
	public void Test_Tower_Model(){
		Tower tower = new Tower(Constants.TOWER1,1,2);
		assertEquals(tower.getDamage(),20);
		assertEquals(tower.getRow(),112.5,0.00000001);
		assertEquals(tower.getCol(),187.5,0.00000001);
		assertEquals(tower.getIntRow(),1);
		assertEquals(tower.getIntCol(),2);
		assertEquals(tower.getFiringRange(),200,0.0001);
		assertEquals(tower.getFiringRate(),2,0.0001);
		assertEquals(tower.getImage(),Constants.MACHINEGUN);
		assertEquals(tower.toString().charAt(0),'T');
	}	
	@Test
	public void Test_Enemy_Model(){
		Enemy enemy = new Enemy("KNIGHT");
		assertEquals(enemy.getDamage(),10);
		enemy.removeHP(20);
		assertEquals(enemy.getSpeed(),50,0.01);
		assertEquals(enemy.getHealth(),480,0.01);
		assertEquals(enemy.getType(),"KNIGHT");
		assertEquals(enemy.toString(),"KNIGHT");
		assertEquals(enemy.getImage(),"/image/KNIGHT.png");
		assertTrue(enemy.unFreeze());
		assertEquals(enemy.getFromRow(),998,0.1);
		assertEquals(enemy.getFromCol(),998,0.1);
		assertEquals(enemy.getToRow(),999,0.1);
		assertEquals(enemy.getToCol(),999,0.1);
		enemy.setID(1);
		assertEquals(enemy.getID(),1);
		assertEquals(enemy.getEndRow(),0,0.1);
		assertEquals(enemy.getEndCol(),0,0.1);
		assertTrue(enemy.setSpawn(true));
		assertTrue(enemy.setFrozen(true));
		assertTrue(enemy.isFrozen());
		assertTrue(enemy.isSpawn());
	}
	@Test
	public void Test_Position_Model(){
		Position position = new Position(0,0);
		assertTrue(position.setBlocked());
		assertTrue(position.setBase());
		assertFalse(position.setBase());
		assertTrue(position.isBase());
		assertTrue(position.isBlocked());
		assertEquals(position.getEnemies().size(),0);
		assertThrows(InvalidPlacementException.class, () -> {
			position.setTower(new Tower(Constants.TOWER1,0,0), 2000);
		});
	}
	@Test
	public void Test_Level_Model(){
		Level level = new Level("STAGE1");
		new Thread(level).start();
		level.spawnEnemy();
		
		assertEquals(level.getRows(),8);
		assertEquals(level.getCols(),8);
		assertThrows(InvalidPlacementException.class, () -> {
			assertTrue(level.setPos(0, 0, Constants.TOWER1));
		});

	}
	@Test
	public void Test_Level_Model_2(){
		Level level = new Level("STAGE1");
		new Thread(level).start();
		level.spawnEnemy();

		assertEquals(level.getRows(),8);
		assertEquals(level.getCols(),8);
		assertThrows(InvalidPlacementException.class, () -> {
			assertTrue(level.setPos(0, 0, Constants.TOWER1));
		});

	}

	@Test
	public void GetBounty() {
		assertEquals(EnemyType.KNIGHT.getBounty(), 75);
	}

	@Test
	public void getBlockedChance() {
		assertEquals(Stage.STAGE1.getBlockedChance(), 0);
	}

	@Test
	public void testTowerType() {
		assertEquals(TowerType.BASIC.getColumns(), 4);
		assertEquals(TowerType.BASIC.getCount(), 4);
		assertEquals(TowerType.BASIC.getOffsetX(), 0);
		assertEquals(TowerType.BASIC.getOffsetY(), 0);
		assertEquals(TowerType.BASIC.getWidth(), 75);
		assertEquals(TowerType.BASIC.getHeight(), 75);
		assertEquals(TowerType.BASIC.getDescr(), "($500) The averagest average tower. Fires average, has average damage, has " +
				"surprisingly good range");
	}

	@Test
	public void testMessages() {
		AddTowerMessage atm = new AddTowerMessage(new Tower("BASIC", 0, 0), 0, 0, 0);
		atm.getTower();
		DamageBaseMessage dbm = new DamageBaseMessage(0, 0, 0);
		assertEquals(dbm.getDamage(), 0);
		EnemyMoveMessage emm = new EnemyMoveMessage(new TreeMap<>());
		emm.getList();
		KilledEnemyMessage kem = new KilledEnemyMessage(0.0, 0.0, 0);
		kem.getBounty();
		kem.getRow();
		kem.getCol();
		RemoveTowerMessage rtm = new RemoveTowerMessage(0,0);
		SellTowerMessage stm = new SellTowerMessage(0,0,"");
		stm.getType();
		ShootBulletMessage sbm = new ShootBulletMessage(0,0,0.0,0.0, "");
		sbm.getType();
		sbm.getRow();
		sbm.getCol();
		sbm.getToRow();
		sbm.getToCol();
	}


}
