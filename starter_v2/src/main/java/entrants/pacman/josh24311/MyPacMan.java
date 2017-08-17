package entrants.pacman.josh24311;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;



/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController
{
    private MOVE myMove = MOVE.NEUTRAL;
    
    public MOVE getMove(Game game, long timeDue)
    {
        // Place your game logic here to play the game as Ms Pac-Man
        
    	
    	int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();
        //int minDistance = Integer.MAX_VALUE;
        int minDistanceGh = Integer.MAX_VALUE;
        int minDistanceEdGh = Integer.MAX_VALUE;
        int closestPp = 0;
        int closestP = 0;

        int disTonearestPp = 0;
        int disTonearestP = 0;
        //int disMinGhAndCloPp = 0;
        int nghWithNp = 0;
        int ghostLocation_now = 2;
        int ghlairtimemux = 1;
        int ParameterCount = 13;
        
        int D[] = new int[ParameterCount];
		
        boolean ambush_stat = false ;
        int mapNow = 0;
       

        Constants.GHOST minGhost = null;
        Constants.GHOST minEdGhost = null;

        // �����H��Int 0~4
        Random rn = new Random();
        int randdir = rn.nextInt(5);
        //�}��Ūpamameter
        try{
        	File myFile = new File("D:\\new.txt");
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);
            
            for(int i = 0;i<ParameterCount;i++){
            	D[i] = Integer.valueOf(reader.readLine());
            }
            
            reader.close();
    	}catch(IOException ex){
    		ex.printStackTrace();
    	}

        //��ܬY�I���Ҧ��i���F�~
        //game.showNeighbour(1167);
        //�P�_�{�b�����@��map
        if(game.getCurrentMaze().name.equalsIgnoreCase("a")){
        	mapNow = 0;
        }
        else if(game.getCurrentMaze().name.equalsIgnoreCase("b")){
        	mapNow = 1;
        }
        else if(game.getCurrentMaze().name.equalsIgnoreCase("c")){
        	mapNow = 2;
        }
        else{
        	mapNow = 3;
        }
        
        //���i������̪�
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If can't see these will be -1 so all fine there
            // ���Ҧ�����
            if (game.getGhostEdibleTime_new(ghost) == 0 && game.getGhostLairTime_new(ghost) == 0)
            {
                // �p�G�o�䰭���i���A�B���bŢ�l��
                int ghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromGh = game.getShortestPathDistance(current, ghostLocation);
                if (disFromGh < minDistanceGh)
                {
                    minDistanceGh = disFromGh;
                    minGhost = ghost;
                }
            }
            else if(game.getGhostLairTime(ghost)!=0)
            {
            	ghlairtimemux = ghlairtimemux * game.getGhostLairTime(ghost);
            	//�Yfor �]�� ����>0����bŢ�l��
            }
        }

        //�i������̪�
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If it is > 0 then it is visible so no more PO checks
            //���Ҧ�����
            if (game.getGhostEdibleTime_new(ghost) > 0)
            {
                //�p�G�o�䰭�B��i�����A
                int EdghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromEdGh = game.getShortestPathDistance(current, EdghostLocation);
                //���o������p���F�̪�Z��
                if (disFromEdGh < minDistanceEdGh)
                {
                    //����FOR�j�餧��|���Z���̵u���i����
                    minDistanceEdGh = disFromEdGh;
                    minEdGhost = ghost;
                }
            }
        }

        //�T�{PP�O�_�s�b
        ArrayList<Integer> targets = new ArrayList<Integer>();
        for (int i = 0; i < powerPills.length; i++)   // check with power pills
        {
            // are available
            Boolean PowerpillStillAvailable = game.isPowerPillStillAvailable_new(i);
            // �o�������~�ӡA���אּgame.isPowerPillStillAvailable(i);
            if (PowerpillStillAvailable != null)
            {
                if (PowerpillStillAvailable)
                {
                    targets.add(powerPills[i]);
                    // �p�G�o�Ӥj�O�Y�s�b�A�h�s�Jtarget
                }
            }
        }
        //�T�{P�O�_�s�b
        ArrayList<Integer> targets_p = new ArrayList<Integer>();
        for (int i = 0; i < pills.length; i++)   // check with pills are available
        {
            Boolean pillStillAvailable = game.isPillStillAvailable_new(i);
            if (pillStillAvailable != null)
            {
                if (pillStillAvailable)
                {
                    targets_p.add(pills[i]);
                    // �p�G�o���ĤY�s�b�A�h�s�Jtarget_p
                }
            }
        }
        if(!targets_p.isEmpty())  //�٦�p
        {
        	//System.out.println("�٦�p");
            //�ഫ
            int[] targetsArray_p = new int[targets_p.size()];
            for (int i = 0; i < targetsArray_p.length; i++)
            {
                targetsArray_p[i] = targets_p.get(i);
            }
            closestP = game.getClosestNodeIndexFromNodeIndex(current, targetsArray_p, Constants.DM.PATH);
            disTonearestP = game.getShortestPathDistance(current, closestP);
        }
        if (!targets.isEmpty())   //�٦�PP�s�b�����p
        {
            //�ഫ
            int[] targetsArray = new int[targets.size()]; // convert from
            // ArrayList to
            // array
            for (int i = 0; i < targetsArray.length; i++)
            {
                targetsArray[i] = targets.get(i);
            }
            closestPp = game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH);
            disTonearestPp = game.getShortestPathDistance(current, closestPp);   
			//�P�_���}�l=====================================================================
            // ���i�����s�b
            if(minEdGhost!=null)
            {
                if(minDistanceEdGh<D[12])
                {
                    //System.out.println("Situation 1 : Go hunting");
                    return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
                }
                if(minGhost!=null)
                {
                    if(minDistanceEdGh>D[12]&&minDistanceGh>D[1] &&minDistanceGh<=D[2]&&disTonearestP<=D[9])
                    {
                        //System.out.println("Situation 9 : Avoid danger eat n_p first");
                        return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                    }
                }
            }
            else //�i�������s�b
            {
                if(minDistanceGh>=D[1] && minDistanceGh <=D[2] && disTonearestPp >=D[5] && disTonearestPp <=D[6])
                {
                    if(mapNow ==0)
                    {
                        //System.out.print("a");
                        if(current==1149||current==1095||current==1125||current==1155||current==1142||current==1136||current==1154||current==1160||current==91||current==85||current==103||current==109||current==96||current==90||current==108||current==114)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                    }
                    else if(mapNow ==1)
                    {
                        if(current==132||current==133||current==221||current==227||current==219||current==218||current==226||current==232||current==1084||current==1085||current==1151||current==1157||current==1149||current==1148||current==1156||current==1162)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                    }
                    else if(mapNow ==2)
                    {
                        if(current==115||current==109||current==127||current==133||current==114||current==120||current==132||current==138||current==1022||current==1023||current==1100||current==1106||current==1098||current==1097||current==1105||current==1111)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                    }
                    else if(mapNow ==3)
                    {
                        if(current==137||current==131||current==149||current==155||current==142||current==136||current==154||current==160||current==1164||current==1158||current==1176||current==1182||current==1169||current==1163||current==1181||current==1187)
                        {
                            ambush_stat = true;
                            //System.out.println("Ambush NOW");
                        }
                    }
                    //�w�ƮI��϶��A��ı���M�I
                    //System.out.println("Coming Ghost: " + minGhost);

                    if(ambush_stat) //�bhide���A�U
                    {
                        if(minDistanceGh<=D[0])
                        {
                            //r3 hide���A�B���l��
                            //���ӱ���
                            ambush_stat = false;
                            //System.out.println("Situation 3 : Too Close Eat PP");
                            //eat pp
                            return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                        }
						
                        if(minDistanceGh >=D[1])
                        {
                            //r4 hide���A�B������
                            //�o�̭n�ץ�
                            //����������
                            ambush_stat = false;
                            //System.out.println("Situation 4 : Ghost GO Away Eat nearest p");
                            //eat n_p
                            return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                        }

                        //�����@
                        if(mapNow ==0) //MAZE_A
                        {
                            switch(current)
                            {
                            case 85:
                                return MOVE.UP;
                            case 91:
                                return MOVE.UP;
                            case 103:
                                return MOVE.DOWN;
                            case 109:
                                return MOVE.DOWN;
                            case 96:
                                return MOVE.UP;
                            case 90:
                                return MOVE.UP;
                            case 108:
                                return MOVE.DOWN;
                            case 114:
                                return MOVE.DOWN;
                            case 1095:
                                return MOVE.UP;
                            case 1125:
                                return MOVE.UP;
                            case 1149:
                                return MOVE.DOWN;
                            case 1155:
                                return MOVE.DOWN;
                            case 1142:
                                return MOVE.UP;
                            case 1136:
                                return MOVE.UP;
                            case 1154:
                                return MOVE.DOWN;
                            case 1160:
                                return MOVE.DOWN;
                            }
                        }
                        else if(mapNow ==1)
                        {
                            switch(current)
                            {
                            case 132:
                                return MOVE.RIGHT;
                            case 133:
                                return MOVE.RIGHT;
                            case 221:
                                return MOVE.DOWN;
                            case 227:
                                return MOVE.DOWN;
                            case 218:
                                return MOVE.LEFT;
                            case 219:
                                return MOVE.LEFT;
                            case 226:
                                return MOVE.DOWN;
                            case 232:
                                return MOVE.DOWN;
                            case 1084:
                                return MOVE.RIGHT;
                            case 1085:
                                return MOVE.RIGHT;
                            case 1151:
                                return MOVE.DOWN;
                            case 1157:
                                return MOVE.DOWN;
                            case 1148:
                                return MOVE.LEFT;
                            case 1149:
                                return MOVE.LEFT;
                            case 1156:
                                return MOVE.DOWN;
                            case 1162:
                                return MOVE.DOWN;
                            }
                        }
                        else if(mapNow ==2)
                        {
                            switch(current)
                            {
                            case 109:
                                return MOVE.UP;
                            case 115:
                                return MOVE.UP;
                            case 127:
                                return MOVE.DOWN;
                            case 133:
                                return MOVE.DOWN;
                            case 114:
                                return MOVE.UP;
                            case 120:
                                return MOVE.UP;
                            case 132:
                                return MOVE.DOWN;
                            case 138:
                                return MOVE.DOWN;
                            case 1022:
                                return MOVE.RIGHT;
                            case 1023:
                                return MOVE.RIGHT;
                            case 1100:
                                return MOVE.DOWN;
                            case 1106:
                                return MOVE.DOWN;
                            case 1097:
                                return MOVE.LEFT;
                            case 1098:
                                return MOVE.LEFT;
                            case 1105:
                                return MOVE.DOWN;
                            case 1111:
                                return MOVE.DOWN;
                            }
                        }
                        else if(mapNow ==3)
                        {
                            switch(current)
                            {
                            case 131:
                                return MOVE.UP;
                            case 137:
                                return MOVE.UP;
                            case 149:
                                return MOVE.DOWN;
                            case 155:
                                return MOVE.DOWN;
                            case 136:
                                return MOVE.UP;
                            case 142:
                                return MOVE.UP;
                            case 154:
                                return MOVE.DOWN;
                            case 160:
                                return MOVE.DOWN;
                            case 1158:
                                return MOVE.UP;
                            case 1164:
                                return MOVE.UP;
                            case 1176:
                                return MOVE.DOWN;
                            case 1182:
                                return MOVE.DOWN;
                            case 1163:
                                return MOVE.UP;
                            case 1169:
                                return MOVE.UP;
                            case 1181:
                                return MOVE.DOWN;
                            case 1187:
                                return MOVE.DOWN;
                            }
                        }
                        //System.out.println("Ambush~~~");

                    }
                    else //�Dambush���A����ı���M�I
                    {
                        //System.out.println("Situation 2 : In Danger Find Somewhere To Hide");
                        switch(closestPp)
                        {
                        case 1143:
                            return game.getNextMoveTowardsTarget(current, 1125, Constants.DM.PATH);
                        case 1148:
                            return game.getNextMoveTowardsTarget(current, 1142, Constants.DM.PATH);
                        case 97:
                            return game.getNextMoveTowardsTarget(current, 103, Constants.DM.PATH);
                        case 102:
                            return game.getNextMoveTowardsTarget(current, 108, Constants.DM.PATH);
                        case 131:
                            return game.getNextMoveTowardsTarget(current, 132, Constants.DM.PATH);
                        case 220:
                            return game.getNextMoveTowardsTarget(current, 219, Constants.DM.PATH);
                        case 1083:
                            return game.getNextMoveTowardsTarget(current, 1084, Constants.DM.PATH);
                        case 1150:
                            return game.getNextMoveTowardsTarget(current, 1149, Constants.DM.PATH);
                        case 121:
                            return game.getNextMoveTowardsTarget(current, 127, Constants.DM.PATH);
                        case 126:
                            return game.getNextMoveTowardsTarget(current, 132, Constants.DM.PATH);
                        case 1021:
                            return game.getNextMoveTowardsTarget(current, 1022, Constants.DM.PATH);
                        case 1099:
                            return game.getNextMoveTowardsTarget(current, 1098, Constants.DM.PATH);
                        case 143:
                            return game.getNextMoveTowardsTarget(current, 137, Constants.DM.PATH);
                        case 148:
                            return game.getNextMoveTowardsTarget(current, 142, Constants.DM.PATH);
                        case 1170:
                            return game.getNextMoveTowardsTarget(current, 1176, Constants.DM.PATH);
                        case 1175:
                            return game.getNextMoveTowardsTarget(current, 1181, Constants.DM.PATH);
                        }
                    }
                }
                
				if(minDistanceGh<=D[0] && disTonearestPp <D[4])
                {
                    //System.out.println("Situation 5 : Avoid tunnel Eat PP now");
                    //eat pp
                    return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                }
                
				if(minDistanceGh>=D[3] &&disTonearestPp >=D[7] &&disTonearestP<=D[8])
                {
                    //System.out.println("Situation 6 : Gh and pp too far Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
                
				if(minDistanceGh>=D[3]&&disTonearestPp <=D[7] && disTonearestP<=D[10])
                {
                    //System.out.println("Situation 8: gh too far AVOID eat pp too early Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
            }
		}
		else //�S��PP
		{
			if(minEdGhost!=null) //���i����
            {
                if(minDistanceEdGh<D[12])
                {
                    //System.out.println("Eat Nearest Edgh");
                    return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
                }
				if(minGhost!=null)
				{
					ghostLocation_now = game.getGhostCurrentNodeIndex_new(minGhost);
					if(minDistanceGh<=D[0]&&disTonearestP<=D[11])
					{
						//r7 Then avoid ghost and eat nearest pill
						//���� avoid 
						return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
					}
					if(minDistanceEdGh>D[12] && minDistanceGh>D[1] &&minDistanceGh<=D[2] && disTonearestP<=D[9])
					{
						// r9 ����ت��A�� �Z���A�� ���YP
						//System.out.println("Edgh too far Eat p first");
						return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
					}
				}
            }
            else//�S���i����
            {
                //10
                //���D: �S��PP�S���i���� �A���ۤv�S���F�@���A���ɴN�S�� minGhost ���ȤF*******************************
                //�ո�: ���bŢ�l�̪��o�q�ɶ����H����
                if(minGhost!=null)//�����bŢ�l��
                {
                    ghostLocation_now = game.getGhostCurrentNodeIndex_new(minGhost);
                    nghWithNp = game.getShortestPathDistance(ghostLocation_now, closestP);
                    if(minDistanceGh<=D[0] && nghWithNp<=D[2])
                    {
                        //System.out.println("Situation 10 : Eat  remain p");
                        return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                    }
                    else // ���װ����k
                    {
                        //�o�̭n��@�����������k
                        //System.out.println("Avoiding too close ghost");
                        return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
                    }
                }
                else //���bŢ�l�̡A���ɨS��PP�A�S���i����
                {
                    switch (randdir)
                    {
                    case 0:
                        myMove = MOVE.UP;
                        break;
                    case 1:
                        myMove = MOVE.RIGHT;
                        break;
                    case 2:
                        myMove = MOVE.DOWN;
                        break;
                    case 3:
                        myMove = MOVE.LEFT;
                        break;
                    case 4:
                        myMove = MOVE.NEUTRAL;
                        break;
                    }
                    return myMove;
                }

            }
		}
      
		
		
		
		
		
		//System.out.println("Not in rule");
		return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        


        //dis(pac,n_gh)   - minDistanceGh
        //dis(pac,n_edgh) - minDistanceEdGh
        //dis(pac,n_pp)   - disTonearestPp
        //dis(pac,n_p)    - disTonearestP
        
        //System.out.println("=================go random=================");
        /*
        switch (randdir)
        {
        case 0:
            myMove = MOVE.UP;
            break;
        case 1:
            myMove = MOVE.RIGHT;
            break;
        case 2:
            myMove = MOVE.DOWN;
            break;
        case 3:
            myMove = MOVE.LEFT;
            break;
        case 4:
            myMove = MOVE.NEUTRAL;
            break;
        }*/
		
    	
    }
}
