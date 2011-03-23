###ARRAY
float ds=5;
int sel = 0;
boolean dragging=false;
PFont fontA = loadFont("Arial");
textFont(fontA, 12);
void mouseDragged(){dragging=true;}
void mouseReleased(){dragging=false;}
void setup(){
frameRate(15);
###SIZE
strokeWeight(1);
###NODES
}
void draw(){
background(255);
stroke(100);
###EDGES
for (int j=0;j < count;j++){
noStroke();
float radi=e[j][2];
float diam=radi/2;
if( dist(e[j][0],e[j][1],mouseX,mouseY) < diam){
fill(64,187,128,200);
sel=1;
if(dragging){
e[j][0]=mouseX;
e[j][1]=mouseY;
}
} else {
if(label[j] == me)
fill(100,0,157,150);
else
fill(30,120,157,150);
sel=0;
}
ellipse(e[j][0],e[j][1],radi,radi);
if(sel==1){
fill(255,255,255,255);
stroke(128,255,0,100);
} else {            
fill(0,0,0,255);
stroke(10,60,60,255);
}
noStroke();
stroke(255);   
ellipse(e[j][0],e[j][1],ds,ds);

fill(10,10,10,255)
text(label[j], e[j][0]+5, e[j][1]+5);
}
}