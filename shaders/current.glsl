//vertex shader
attribute vec4 position;
attribute vec2 uv;
void main() {
	gl_Position = position;
}

//===========================================
//fragment shader

precision mediump float;
void main() {
	gl_FragColor = vec4(0.6,0.1,0.1,1.0);
}