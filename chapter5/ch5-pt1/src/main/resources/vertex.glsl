#version 330

// stating from position 0 we are expecting a vector composed of 3 attributes
layout (location =0) in vec3 position;

void main() {

    gl_Position = vec4(position, 1.0);
}
