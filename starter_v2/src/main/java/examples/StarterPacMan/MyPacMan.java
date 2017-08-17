package examples.StarterPacMan;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Random;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController {
    private static final int MIN_DISTANCE = 20;
    private Random random = new Random();

    @Override
    public MOVE getMove(Game game, long timeDue) {

        // Should always be possible as we are PacMan
    	//���o�ثe�p���F�Ҧbindex
        int current = game.getPacmanCurrentNodeIndex();

        // Strategy 1: Adjusted for PO
        //���ײĤ@�ӧ��B�Z���p��Y�Ȫ���
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // If can't see these will be -1 so all fine there
        	//���Ҧ�����
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
            	//�p�G�o�䰭���i���A�B���bŢ�l��
                int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
                //���o����index
                if (ghostLocation != -1) {
                    if (game.getShortestPathDistance(current, ghostLocation) < MIN_DISTANCE) {
                    	//�p�G�p���F�򦹰����̵u�Z���p��MIN_DISTANCE
//                        System.out.println("Evading Ghost");
                        return game.getNextMoveAwayFromTarget(current, ghostLocation, Constants.DM.PATH);
                        //�^�ǻ����������k�A�ǧ�FOR�j��N�����F
                    }
                }
            }
        }

        /// Strategy 2: Find nearest edible ghost and go after them
        int minDistance = Integer.MAX_VALUE;
        Constants.GHOST minGhost = null;
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // If it is > 0 then it is visible so no more PO checks
        	//���Ҧ�����
            if (game.getGhostEdibleTime(ghost) > 0) {
            	//�p�G�o�䰭�B��i�����A
                int distance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));
                //���o������p���F�̪�Z��
                if (distance < minDistance) {
                	//����FOR�j�餧��|���Z���̵u���i����
                    minDistance = distance;
                    minGhost = ghost;
                }
            }
        }

        if (minGhost != null) {
        	//�p�G���o�˪��i�����s�b
//            System.out.println("Hunting Ghost");
            return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);
            //�^�ǩ����i�������e�i��V
        }

        // Strategy 3: Go after the pills and power pills that we can see
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();

        ArrayList<Integer> targets = new ArrayList<Integer>();
        
        for (int i = 0; i < pills.length; i++) {
            //check which pills are available
            Boolean pillStillAvailable = game.isPillStillAvailable(i);
            if (pillStillAvailable != null) {
                if (pillStillAvailable) {
                    targets.add(pills[i]);
                    //�p�G�o�Ӥp�ĤY�s�b�A�h�s�Jtarget
                }
            }
        }

        for (int i = 0; i < powerPills.length; i++) {            //check with power pills are available
            Boolean pillStillAvailable = game.isPowerPillStillAvailable(i);
            //�o�������~�ӡA���אּgame.isPowerPillStillAvailable(i);
            if (pillStillAvailable != null) {
                if (pillStillAvailable) {
                    targets.add(powerPills[i]);
                    //�p�G�o�Ӥj�O�Y�s�b�A�h�s�Jtarget
                }
            }
        }

        if (!targets.isEmpty()) {
            int[] targetsArray = new int[targets.size()];        //convert from ArrayList to array

            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }
            //return the next direction once the closest target has been identified
//            System.out.println("Hunting pill");
            return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH), Constants.DM.PATH);
            //�^�Ǥp���F��targetArray�Ĥ@�Ӫ��󪺲��ʤ�V
        }


        // Strategy 4: New PO strategy as now S3 can fail if nothing you can see
        // Going to pick a random action here
        MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
        if (moves.length > 0) {
            return moves[random.nextInt(moves.length)];
        }

//        System.out.println("Just turn around");
        // Must be possible to turn around
        return game.getPacmanLastMoveMade().opposite();
    }
}