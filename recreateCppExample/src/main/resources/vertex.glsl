#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aTextureCoordinate;

out vec4 outputColor;
out vec2 textureCoordinate;

uniform float xAxisOffset;

void main()
{
    gl_Position = vec4(aPos.x + xAxisOffset, aPos.y, aPos.z, 1.0);
	outputColor = vec4(aColor, 1.0);
	textureCoordinate = aTextureCoordinate;
}