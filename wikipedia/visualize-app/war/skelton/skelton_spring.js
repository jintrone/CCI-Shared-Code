var ParticleEngine = function() {
	this.particles = new Array();
	this.springs = new Array();
	
	this.addParticle = function(particle) {
		particle.setEngine(this);
		this.particles.push(particle);
	};
	
	this.addSpring = function(spring) {
		spring.setEngine(this);
		this.springs.push(spring);
	};
	
	this.update = function() {
		var i = 0;
		while (i < this.springs.length) {
			var s = this.springs[i];
			s.update();
			if (!s.destroyed) {
				i++;
			} else {
				this.springs.slice(i,1);
			}
		}
		i = 0;
		while (i < this.particles.length) {
			var p = this.particles[i];
			p.update();
			if (!p.destroyed) {
				i++;
			} else {
				this.particles.slice(i,1);
			}
		}
	};
	
	this.draw = function() {
		for (int i = 0; i < this.springs.length; i++) {
			var s = this.springs[i];
			s.draw();
		}
		strokeWeight(1);
		for (int i = 0; i < this.particles.length; i++) {
			var p = this.particles[i];
			p.draw();
		}
	};
	
	this.particleAt = function(x,y) {
		int i = this.particles.length;
		while (i > 0) {
			i--;
			var p = this.particles[i];
			var dx = p.x - x;
			var dy = p.y - y;
			if (dx * dx + dy * dy < p.size * p.size) {
				return p;
			}
		}
		return null;
	};
	
	this.findParticle = function(label) {
		int i = this.particles.length();
		while (i > 0) {
			i--;
			var p = this.particles[i];
			
			if (p.label == label) {
				return p;
			}
		}
		return null;
	};
	
	this.connectParticles = function(label1, label2, thick) {
		var particle1 = this.findParticle(label1);
		var particle2 = this.findParticle(label2);
		if (particle1 != null && particle2 != null) {
			this.addSpring(new Spring(particle1, particle2, thick, 100, 0.02));
		}
	};
}

var Spring = function(start, end, thick, springLength, springConstant) {
	this.start = start;
	this.end = end;
	this.thick = thick;
	this.springLength = springLength;
	this.springConstant = springConstant;
	
	this.setEngine = function(engine) {
		this.engine = engine;
	};
	
	this.update = function() {
		var dx = this.end.x - this.start.x;
		var dy = this.end.y - this.start.y;
		var d = sqrt(dx*dx + dy*dy);

		if (d > 0) {
			dx /= d;
			dy /= d;
		}
		d -= this.springLength;
		d *= this.springConstant;
		
		this.start.velocityX += d*dx;
		this.start.velocityY += d*dy;
		this.end.velocityX -= d*dx;
		this.end.velocityY -= d*dy;
	};
	
	this.draw = function() {
		noFill();
		stroke(100);
		strokeWeight(this.thick);
		line(this.start.x, this.start.y, this.end.x, this.end.y);
	};
}

var Particle = function(label, x, y, size, velocityX, velocityY, col) {
	this.label = label;
	this.x = x;
	this.y = y;
	this.size = size;
	this.velocityX = velocityX;
	this.velocityY = velocityY;
	
	this.gravityX = random(-0.1,0.1);
	this.gravityY = random(-0.1,0.1);
	
	this.damping = 0.3;
	this.col = col;
	
	this.life = -1;
	this.living = 0;
	
	this.destroyed = false;
	this.dragged = false;
	this.pinned = false;
	
	this.setEngine = function(engine) {
		this.engine = engine;
	};
	
	this.setPos = function(x, y, velocityX, velocityY) {
		this.x = x;
		this.y = y;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	};
	
	this.update = function() {
		if (this.dragged) {
			this.x = mouseX;
			this.y = mouseY;
			this.velocityX = 0;
			this.velocityY = 0;
			return;
		}
		if (this.pinned) {
			this.velocityX = 0;
			this.velocityY = 0;
			return;
		}
		this.velocityX += this.gravityX;
		this.velocityY += this.gravityY;
		
		this.velocityX *= this.damping;
		this.velocityY *= this.damping;
		
		if ((this.x-this.size) <= 0 || (this.x+this.size) >= canvasSize) {
			this.velocityX = 0;
			this.x -= this.velocityX;
		} else {
			this.x += this.velocityX;
		}
		if ((this.y-this.size) <= 0 || (this.y+this.size) >= canvasSize) {
			this.velocityY = 0;
			this.y -= this.velocityY;
		} else {
			this.y += this.velocityY;
		}

		//this.x += this.velocityX;
		//this.y += this.velocityY;
		if (this.life > 0) {
			this.life--;
			if (this.life <= 0) {
				this.destroyed = true;
			}
		}
	};
	
	this.draw = function() {
		if (this.destroyed) {
			return;
		}
		noStroke();
		fill(this.col);
		
		stroke(150);
		ellipse(this.x, this.y, this.size, this.size);

		fill(0);
		text(this.label, this.x-textWidth(this.label)*0.5, this.y+5);
	};
	
	this.pin = function(x, y) {
		this.x = x;
		this.y = y;
		this.pinned = true;
	};	
}

var engine;
var lastParticle;
var dragging;
#SIZE
PFont font;

void setup() {
	size(canvasSize, canvasSize);
	engine = new ParticleEngine();
	smooth();
	font = createFont("SansSerif",11);
	textFont(font);

#NODES
			
#PINNED
	
#EDGES

}

void draw() {
  //update the particles
  engine.update();
  //clear the background
  background (255);
  //draw the particles
  engine.draw();
}
 
void mousePressed() {
  dragging = engine.particleAt(mouseX, mouseY);
  if (dragging!=null) {
    dragging.dragged = true;
    lastParticle = dragging;
  }
}
 
void mouseReleased() {
  if (dragging!=null) {
    dragging.dragged=false;
    dragging = null;
  }
}