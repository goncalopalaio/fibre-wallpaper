//vertex shader
attribute vec4 position;
attribute vec2 uv;
void main() {
	gl_Position = position;
}

//===========================================
//fragment shader
precision mediump float;

uniform float width;
uniform float height;
uniform float time;

float distLine(vec2 p, vec2 a, vec2 b) {
	vec2 pa = p-a;
	vec2 ba = b-a;
	float t = clamp(dot(pa,ba)/dot(ba,ba), 0.0, 1.0);
	return length(pa-ba*t);
}

float N21(vec2 p) {
	p = fract(p*vec2(3.995643, 1.73));
	p += dot(p, p+4.914);
	return fract(p.x * p.y);
}      
vec2 N22(vec2 p) {
	float n = N21(p);
	return vec2(n, N21(p+n));
}

vec2 getPointInGridCell(vec2 id, vec2 offset) {
	vec2 n = (N22(id + offset)) * time;
	n = n - 0.5;
	return offset + sin(n) * 0.2;
}

float line(vec2 p, vec2 a, vec2 b) {
	float d = distLine(p, a, b);

	float m = smoothstep(0.03, 0.01, d);

	m *= smoothstep(1.2, 0.8, length(a-b)); // do not draw lines if too close
	return m;
}
void main() {
	vec2 uv = (gl_FragCoord.xy - 0.5 * vec2(width, height)) / height;
	
	float m = 0.0;
	
	uv *= 15.0;
	vec2 gv = fract(uv) - 0.5;
	vec2 id = floor(uv) - 0.5;


	int i = 0;
	vec2 p[9];
	for(float y = -1.0; y <=1.0; y++) {
		for(float x = -1.0; x <=1.0; x++) {
			p[i] = getPointInGridCell(id, vec2(x,y));
			i++;
		} 			
	}

	float t = time * 10.;
	for(int i = 0; i < 9; i++) {
		m += line(gv, p[4], p[i]);

		vec2 j = (p[i] - gv) * 20.;
		float sparkle = 1. / dot(j, j);
		m += sparkle * (sin(t+p[i].y * 10.) * .5 + .5);
	}
	m += line(gv, p[1], p[3]);
	m += line(gv, p[1], p[5]);
	m += line(gv, p[7], p[3]);
	m += line(gv, p[7], p[5]);

	vec3 col = vec3(m);
	//col.rg = N22(uv);
	//if (gv.x > 0.48 || gv.y > 0.48) col = vec3(1.0,1.0,0.0);
	
	gl_FragColor = vec4(col, 1.0);
}