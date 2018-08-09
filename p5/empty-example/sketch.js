var particles;
function preload() {
  particles = loadStrings('output.txt');
}

function setup() {
  // put setup code here
  createCanvas(600,600);
  console.log(particles)
  console.log("setup finished!")
}

function draw() {
  // put drawing code here
  background(200);

  for (var i = 0; i < particles.length ; i++) {
  	p_data = particles[i].split(" ")
  	drawParticle(float(p_data[0])*600,float(p_data[1])*600,float(p_data[2])*600,p_data[3])
  };
}

function drawParticle(x, y, radius, color) {
  fill(color);
  ellipse(x, y, radius, radius);
}