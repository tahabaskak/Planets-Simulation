import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NBody {
	private String image;				
	private double positionX;				
	private double positionY;					
	private double mass;					
	private double forceX;					
	private double forceY;					
	private double accelerationX;					
	private double accelerationY;				
	private double velocityX;					
	private double velocityY;					
	private double radius;			
	private int sizeOfUniverse;	
	private Color color;
	


	public static void main(String[] args) throws IOException {
		String algorithm = args[0];
		if(algorithm.equals("brute")){

			String fileName = args[4];									/* input name */
			FileWriter fileWrite =  new FileWriter(args[6]);			/* output name */
			PrintWriter writeInformation =new PrintWriter(fileWrite);	
			ArrayList<NBody> allPlanet = new ArrayList<NBody>();		/* create arrayList for nBody class */
			readFile(fileName,allPlanet);								/* read input file and add arrayList */
			
			double T = Double.parseDouble(args[1]);						/* T time */
			double dT = Double.parseDouble(args[2]);					/* dT time */
			
			writeInformation.println("Double G = 6.67 * Math.pow(10, -11);");	/* G is value */
			writeInformation.println("T = " + T);								/* T value */
			writeInformation.println("dT = " + dT + "\n");						/* dT value */
			
			writeInformation.println(args[4] + "\n");							/* write input name */
			writeInformation.println("input\n");
			
			if(allPlanet.get(0).getImage() != null){							/* if input include planet */
				for(NBody p : allPlanet){										/* write planets information on file */
					writeInformation.println(p.getPositionX() + " " + p.getPositionY() + " " + p.getVelocityX() 
					+ " " + p.getVelocityY() + " " + p.getMass() + " " + p.getImage());
				}
			}
			else{																/* if input include particle */
				for(NBody p : allPlanet){										/* write particles information on file */
					writeInformation.println(p.getPositionX() + " " + p.getPositionY() + " " + p.getVelocityX() 
					+ " " + p.getVelocityY() + " " + p.getMass() + " " + p.getColor());
				}
			}
			
			double radius = allPlanet.get(0).getRadius();						/* radius value */
	        StdDraw.show(0);
	        StdDraw.setXscale(-radius, +radius);								/* Calculated StdDraw range */
	        StdDraw.setYscale(-radius, +radius);
	        
	        if(allPlanet.get(0).getImage() != null){							/* if input is planet */
				for (double t = 0.0; t<T; t = t + dT){							/* for T%dT time */
					StdDraw.clear();
					StdDraw.picture(0, 0,  "images/starfield.jpg");				/* picture background image */
					for(NBody p : allPlanet)									/* picture images in input on background image */
						StdDraw.picture(p.getPositionX(), p.getPositionY(), "images/" + p.getImage());
					 
					findForceAllPlanetEachOther(allPlanet);						/* find Forces */
					findAccelerationAllPlanet(allPlanet);						/* find accelerations */
					findVelocityAllPlanet(allPlanet, dT);						/* find velocitys */
					findLocationAllPlanet(allPlanet, dT);						/* find new locations */
					StdDraw.show(10);
				}
				writeInformation.println("\n" + "output\n");
				for(NBody p : allPlanet){										/* write last information on file */
					writeInformation.println(p.getPositionX() + " " + p.getPositionY() + " " + p.getVelocityX() 
					+ " " + p.getVelocityY() + " " + p.getMass() + " " + p.getImage());
				}
	        }
	        else{														/* if input is particle */
	        	for (double t = 0.0; t<T; t = t + dT){					/* for T%dT time */
	        		StdDraw.clear(StdDraw.BLACK);						/* picture background image */
	        		
					for(NBody p : allPlanet){							/* picture particle in input on background image */
						StdDraw.setPenColor(p.getColor());
		            	StdDraw.point(p.getPositionX(), p.getPositionY());
					}

					findForceAllParticleEachOther(allPlanet);					/* find Forces */
					findAccelerationAllPlanet(allPlanet);						/* find accelerations */
					findVelocityAllPlanet(allPlanet, dT);						/* find velocitys */
					findLocationAllPlanet(allPlanet, dT);						/* find new locations */
					     
					StdDraw.show(10);
				}
	        	writeInformation.println("\n" + "output\n");
				for(NBody p : allPlanet){									/* write last information on file */
					writeInformation.println(p.getPositionX() + " " + p.getPositionY() + " " + p.getVelocityX() 
					+ " " + p.getVelocityY() + " " + p.getMass() + " " + p.getColor());
				}
	        }
	       allPlanet.clear();							/* clear allPlanet */
	       writeInformation.close();					/* close write file */
		}
		
	}
	
	public static void findLocationAllPlanet(ArrayList<NBody> allPlanet,double deltaT){
		for(NBody p : allPlanet){
			if(p.getVelocityX()>=0){				/* positionX bigger zero so positive value adding */
				p.setPositionX(p.getPositionX() + (deltaT*Math.abs(p.getVelocityX())));
			}
			else{									/* positionX bigger zero so negative value adding */
				p.setPositionX(p.getPositionX() - (deltaT*Math.abs(p.getVelocityX())));
			}
			if(p.getVelocityY()>=0){				/* positionY bigger zero so positive value adding */
				p.setPositionY(p.getPositionY() + (deltaT*Math.abs(p.getVelocityY())));
			}
			else{									/* positionY bigger zero so negative value adding */
				p.setPositionY(p.getPositionY() - (deltaT*Math.abs(p.getVelocityY())));
			}
		}
	}
	
	public static void findVelocityAllPlanet(ArrayList<NBody> allPlanet,double deltaT){
		for(NBody p : allPlanet){
			if(p.getForceX()>=0){					/* velocityX bigger zero so negative value adding */
				p.setVelocityX(p.getVelocityX() + (p.getAccelerationX()*deltaT));
			}	
			else{									/* velocityX bigger zero so negative value adding */
				p.setVelocityX(p.getVelocityX() - (p.getAccelerationX()*deltaT));
			}
			if(p.getForceY()>=0){					/* velocityY bigger zero so negative value adding */
				p.setVelocityY(p.getVelocityY() + (p.getAccelerationY()*deltaT));
			}
			else{									/* velocityY bigger zero so negative value adding */
				p.setVelocityY(p.getVelocityY() - (p.getAccelerationY()*deltaT));
			}
		}
	}
	
	public static void findAccelerationAllPlanet(ArrayList<NBody> allPlanet){
		for(NBody p : allPlanet){
			p.setAccelerationX(Math.abs(p.getForceX())/p.getMass());		/* Calculate accelerationX */
			p.setAccelerationY(Math.abs(p.getForceY())/p.getMass());		/* Calculate accelerationY */
		}
	}
	
	public static void findForceAllPlanetEachOther(ArrayList<NBody> allPlanet){
		double force,range,deltaX,deltaY,forceX,forceY,sumForceX,sumForceY;
		double G = 6.67e-11;

		for(int i=0; i<allPlanet.size();i++){
			sumForceX=0;		/* Calculate sum ForceX for i. planet */
			sumForceY=0;		/* Calculate sum ForceXY for i. planet */
			for(int j=0 ; j<allPlanet.size();j++){
				if(!allPlanet.get(i).getImage().equals(allPlanet.get(j).getImage())){	/* Other planet */
					deltaX = allPlanet.get(i).getPositionX() - allPlanet.get(j).getPositionX();	/* calculate deltaX */
					deltaY = allPlanet.get(i).getPositionY() - allPlanet.get(j).getPositionY();	/* calculate deltaY */
					range = Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY, 2));				/* calculate range */
					force = (G*allPlanet.get(i).getMass()*allPlanet.get(j).getMass())/Math.pow(range, 2);	/* calculate force*/
					forceX = force*Math.abs(deltaX)/range;		/* calculate forceX */
					forceY = force*Math.abs(deltaY)/range;		/* calculate forceY */
					
					if(deltaX > 0){								/* deltaX bigger zero so force is negative */
						forceX = (-1*forceX);
					}
					if(deltaY > 0){
						forceY = (-1*forceY);					/* deltaXY bigger zero so force is negative */
					}
					sumForceX = sumForceX + forceX;				
					sumForceY = sumForceY + forceY;
				}
			}
			allPlanet.get(i).setForceX(sumForceX);				/* adding forceX */
			allPlanet.get(i).setForceY(sumForceY);				/* adding forceY */
		}
	}
	
	public static void findForceAllParticleEachOther(ArrayList<NBody> allPlanet){
		double force,range,deltaX,deltaY,forceX,forceY,sumForceX,sumForceY;
		double G = 6.67e-11;
		for(int i=0; i<allPlanet.size();i++){
			sumForceX=0;		/* Calculate sum ForceX for i. planet */
			sumForceY=0;		/* Calculate sum ForceXY for i. planet */
			for(int j=0 ; j<allPlanet.size();j++){
				if(i!=j){
					deltaX = allPlanet.get(i).getPositionX() - allPlanet.get(j).getPositionX();	/* calculate deltaX */
					deltaY = allPlanet.get(i).getPositionY() - allPlanet.get(j).getPositionY();	/* calculate deltaY */
					range = Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY, 2));				/* calculate range */
					force = (G*allPlanet.get(i).getMass()*allPlanet.get(j).getMass())/Math.pow(range, 2);	/* calculate force*/
					forceX = force*Math.abs(deltaX)/range;		/* calculate forceX */
					forceY = force*Math.abs(deltaY)/range;		/* calculate forceY */
					
					if(deltaX > 0){								/* deltaX bigger zero so force is negative */
						forceX = (-1*forceX);
					}
					if(deltaY > 0){
						forceY = (-1*forceY);					/* deltaXY bigger zero so force is negative */
					}
					sumForceX = sumForceX + forceX;				
					sumForceY = sumForceY + forceY;
				}
			}
			allPlanet.get(i).setForceX(sumForceX);				/* adding forceX */
			allPlanet.get(i).setForceY(sumForceY);				/* adding forceY */
		}
	}
	
	public static void readFile(String fileName,ArrayList<NBody> allPlanet){
		try{
			FileReader inputFile = new FileReader(fileName);				
			BufferedReader  bufferReader = new BufferedReader(inputFile);
			String line;
			line = bufferReader.readLine();
			int sizeOfUniverse = Integer.parseInt(line);		/* size of universe */
			line = bufferReader.readLine();
			double radius = Double.parseDouble(line);			/* radius of universe */
			for(int i = 0 ; i<sizeOfUniverse ; i++){
				if((line = bufferReader.readLine()) != null){
					addPlanetInformation(line.trim(), allPlanet, radius, sizeOfUniverse);	/* adding planets information */
				}
			}
			bufferReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void addPlanetInformation(String line,ArrayList<NBody> allPlanet,Double radius,int sizeOfUniverse){
		NBody newPlanet = new NBody();
		String[] splitString = line.trim().split("[\\\t\\  \\ \\   \\    ]");		/* split */
		ArrayList<String> values = new ArrayList<String>();
		for(int i = 0 ; i<splitString.length ; i++){
			if(!splitString[i].isEmpty()){
				values.add(splitString[i]);									/* every information */
			}
		}
		if(values.size() == 6){
			newPlanet.setRadius(radius);
			newPlanet.setSizeOfUniverse(sizeOfUniverse);
			newPlanet.setPositionX(Double.parseDouble(values.get(0)));
			newPlanet.setPositionY(Double.parseDouble(values.get(1)));
			newPlanet.setVelocityX(Double.parseDouble(values.get(2)));
			newPlanet.setVelocityY(Double.parseDouble(values.get(3)));
			newPlanet.setMass(Double.parseDouble(values.get(4)));
			newPlanet.setImage(values.get(5));
			allPlanet.add(newPlanet);
		}
		else{
			newPlanet.setRadius(radius);
			newPlanet.setSizeOfUniverse(sizeOfUniverse);
			newPlanet.setPositionX(Double.parseDouble(values.get(0)));
			newPlanet.setPositionY(Double.parseDouble(values.get(1)));
			newPlanet.setVelocityX(Double.parseDouble(values.get(2)));
			newPlanet.setVelocityY(Double.parseDouble(values.get(3)));
			newPlanet.setMass(Double.parseDouble(values.get(4)));
			Color color = new Color(Integer.parseInt(values.get(5)),Integer.parseInt(values.get(6)),Integer.parseInt(values.get(7)));
			newPlanet.setColor(color);
			allPlanet.add(newPlanet);
		}
		values.clear();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getForceX() {
		return forceX;
	}

	public void setForceX(double forceX) {
		this.forceX = forceX;
	}

	public double getForceY() {
		return forceY;
	}

	public void setForceY(double forceY) {
		this.forceY = forceY;
	}

	public double getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getSizeOfUniverse() {
		return sizeOfUniverse;
	}

	public void setSizeOfUniverse(int sizeOfUniverse) {
		this.sizeOfUniverse = sizeOfUniverse;
	}

}
