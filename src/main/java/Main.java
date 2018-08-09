import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static java.lang.System.exit;

public class Main {

	static int n;
	static int m;
	static double l;
	static boolean isPeriodic;
	static int selectedParticleID;

	static double rc;
	static double maxParticleRadius = 0.5;

	static List<Particle> particles = new ArrayList<>();

	private static void readParametersFromFile(String filename) {

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(filename));

			JSONObject jsonObject = (JSONObject) obj;

			n = ((Long) jsonObject.get("N")).intValue();
			m = ((Long) jsonObject.get("M")).intValue();
			l = (double) jsonObject.get("L");
			rc = (double) jsonObject.get("Rc");
			selectedParticleID = ((Long) jsonObject.get("selectedParticleID")).intValue();
			isPeriodic = (boolean) jsonObject.get("isPeriodic");
			JSONArray JSONparticles = (JSONArray) jsonObject.get("particles");

			Iterator<JSONObject> iterator = JSONparticles.iterator();
			int id = 0;
			while (iterator.hasNext()) {
				JSONObject p = iterator.next();
				particles.add(new ParticleImpl(id++, (double) p.get("r"), rc, (double) p.get("x"), (double) p.get("y")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Filename required");
			exit(1);
		}
		String filename = args[0];

		boolean bruteForce = false;
		if (args.length >= 2 && args[1].equals("brute")) {
			bruteForce = true;
		}

		readParametersFromFile(filename);

		Random random = new Random(24);
//		n = 1000;
//		l = 20.0;
//		rc = 4.0;
//		maxParticleRadius= 0.25;
//		m = 1;
//		isPeriodic = true;
//		particles = generateParticles(n, l, rc, maxParticleRadius, random);
//		generateJSONInput(n, l, rc, maxParticleRadius, random);

		if (l/m < rc + 2*maxParticleRadius) {
			throw new IllegalStateException("L/M < rc + 2r");
		}
		Board board = new Board(m, l, isPeriodic);

    	board.addParticles(particles);

//		System.out.println("Estático:");
//		System.out.println(n);
//		System.out.println(l);
//
//		System.out.println("Dinámico:");
//		System.out.println("t0");
//		particles.forEach(System.out::println);


		try {
			File fout = new File("p5/empty-example/output.txt");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));


			long startTime = System.nanoTime();
			for (int i = 0; i < particles.size(); i++) {
				Collection<Particle> neighbors;
				if (bruteForce) {
					neighbors = board.getInteractingParticlesBruteForce(particles.get(selectedParticleID));
				} else {
					neighbors = board.getInteractingParticles(particles.get(selectedParticleID));
				}

				if (i == selectedParticleID) {
					particles.forEach(particle -> {
						try {
							if (particle.getId() == selectedParticleID) {
								bw.write(particle.getX() / l + " " + particle.getY() / l + " " + particle.getRadius() / l + " red\n");
							} else if (neighbors.contains(particle)) {
								bw.write(particle.getX() / l + " " + particle.getY() / l + " " + particle.getRadius() / l + " green\n");
							} else {
								bw.write(particle.getX() / l + " " + particle.getY() / l + " " + particle.getRadius() / l + " white\n");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			}
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;
			System.out.println("Total time: " + totalTime / 1000000.0 + "ms");
			bw.close();
		} catch (Exception e) { e.printStackTrace(); }

	}



	public static void generateJSONInput(int n, double l, double rc, double maxParticleRadius, Random random) throws IOException{
		JSONObject obj = new JSONObject();
		int out_n = 6000;
		int out_m = 5;

		obj.put("N", out_n);
		obj.put("L", 20.0);
		obj.put("M", out_m);
		obj.put("Rc", 2.0);
		obj.put("isPeriodic", true);
		obj.put("selectedParticleID", Math.abs(random.nextInt())%out_n);

		JSONArray particle_array = new JSONArray();

		for (int i = 0; i < out_n; i++) {
			JSONObject p = new JSONObject();

			double randX = random.nextDouble() * l;
			double randY = random.nextDouble() * l;
			double radius = random.nextDouble() * maxParticleRadius;

			p.put("x", randX);
			p.put("y", randY);
			p.put("r", 0.25);

			particle_array.add(p);
		}
		obj.put("particles", particle_array);

		try (FileWriter file = new FileWriter("input4.json")) {
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
		}
	}


    public static List<Particle> generateParticles(int n, double l, double rc, double maxParticleRadius, Random random) throws IOException{
    	List<Particle> particles = new ArrayList<>();
    	for (int i = 0; i < n; i++) {
    		double randX = random.nextDouble() * l;
    		double randY = random.nextDouble() * l;
    		double radius = random.nextDouble() * maxParticleRadius;

    		particles.add(new ParticleImpl(i, radius, rc, randX, randY));
		}
		return particles;
	}

}
