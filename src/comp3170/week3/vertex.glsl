#version 410

in vec4 a_position;  // Vertex position in NDC
in vec3 a_colour;    // Vertex colour RGB

out vec3 v_colour;   // To fragment shader

uniform mat4 u_modelMatrix; // Model transformation matrix

void main() {
    v_colour = a_colour;
    
    // Apply model matrix transformation
    gl_Position = u_modelMatrix * a_position;
}
