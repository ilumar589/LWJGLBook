#version 330 core

in vec4 outputColor;
in vec2 textureCoordinate;

out vec4 FragColor;

uniform sampler2D ourTexture;

void main()
{
    FragColor = texture(ourTexture, textureCoordinate);
}