import java.util.Random;
public class Individual {
	public static final int SIZE = 91;
	public static final int ParameterCount = 13;
    private int[] genes = new int[SIZE];
	//genes[0]~genes[13]
    private int fitnessValue;

    public Individual() {}

    public int getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public int getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, int gene) {
        this.genes[index] = gene;
		//500�ʭӰ�]�A�C�ӥi��O0��1
    }

    public void randGenes() {
        Random rand = new Random();
        for(int i=0; i<SIZE; ++i) { //0~90
            this.setGene(i, rand.nextInt(2));
        }
    }

    public void mutate() {
        Random rand = new Random();
        int index = rand.nextInt(SIZE);
        this.setGene(index, 1-this.getGene(index));    // flip
    }

    public int evaluate() {
    	int bit=0;    //��2���ɦ��B��
    	int sum = 0;  //�֭p�@��parameter�����`�M
    	int gnow = 0; //�����ثe��]��m
    	int D[] = new int[ParameterCount];
		
		for(int i = 0; i<ParameterCount; i++){
			for(int j = 0; j<SIZE/ParameterCount; j++){
				sum += this.genes[gnow] <<bit;
				bit++;
				gnow++;
			}
			bit = 0;
			D[i] = sum;
			sum = 0;
		}
		
    	
        int fitness = 0;
        double avgs = 0;
    	ExecuteGame play = new ExecuteGame();
    	avgs = play.run(D,1);
    	fitness = (int)avgs;
    	this.setFitnessValue(fitness);
        return fitness;
    }
}
