package comp3170.week3;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ShaderLibrary;

public class Scene {

	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector3f[] colours;
	private int colourBuffer;
	private float theta = 0; // Angle in radians
	private float speed = 1.0f; // Speed of rotation (adjustable)
	private float radius = 0.5f; // Radius of circular path

	private Shader shader;

	public Scene() {

		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);

		// @formatter:off
			//          (0,1)
			//           /|\
			//          / | \
			//         /  |  \
			//        / (0,0) \
			//       /   / \   \
			//      /  /     \  \
			//     / /         \ \		
			//    //             \\
			//(-1,-1)           (1,-1)
			//
	 		
		vertices = new Vector4f[] {
			new Vector4f( 0, 0, 0, 1),
			new Vector4f( 0, 1, 0, 1),
			new Vector4f(-1,-1, 0, 1),
			new Vector4f( 1,-1, 0, 1),
		};
			
			// @formatter:on
		vertexBuffer = GLBuffers.createBuffer(vertices);

		// @formatter:off
		colours = new Vector3f[] {
			new Vector3f(1,0,1),	// MAGENTA
			new Vector3f(1,0,1),	// MAGENTA
			new Vector3f(1,0,0),	// RED
			new Vector3f(0,0,1),	// BLUE
		};
			// @formatter:on

		colourBuffer = GLBuffers.createBuffer(colours);

		// @formatter:off
		indices = new int[] {  
			0, 1, 2, // left triangle
			0, 1, 3, // right triangle
			};
			// @formatter:on

		indexBuffer = GLBuffers.createIndexBuffer(indices);

	}

//	public void draw() {
//	    shader.enable();
//	    
//	    // Set vertex attributes
//	    shader.setAttribute("a_position", vertexBuffer);
//	    shader.setAttribute("a_colour", colourBuffer);
//	    
//	    // Create a model matrix and set it as a uniform
//	    Matrix4f modelMatrix = new Matrix4f();
//	    Matrix4f temp = new Matrix4f();
//	    
//	    // Image at
////	    modelMatrix.identity(); 
//	    
//	    // Image b)
////	    rotationMatrix((float)Math.toRadians(90), modelMatrix);
//	    
//	    // Image c)
////	    modelMatrix.mul(translationMatrix(0.5f, -0.5f, temp));
////	    modelMatrix.mul(scaleMatrix(0.5f, 0.5f, temp));    
//	    
//	    // Image d)
////	    modelMatrix.mul(translationMatrix(-0.65f, 0.65f, temp));
////	    modelMatrix.mul(rotationMatrix((float)Math.toRadians(-45), temp));
////	    modelMatrix.mul(scaleMatrix(0.5f, 0.5f, temp));
//
//	   
//	  
//	    // Example: Apply desired transformation (replace with specific cases below)
//	    // rotationMatrix((float)Math.toRadians(90), modelMatrix);
//	    // scaleMatrix(0.5f, 0.5f, modelMatrix);
//	    // translationMatrix(0.5f, -0.5f, modelMatrix);
//	    
//	    shader.setUniform("u_modelMatrix", modelMatrix);
//	    
//	    // Draw using index buffer
//	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
//	    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
//	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
//	}

	public void draw(float deltaTime) {
	    shader.enable();

	    // Set vertex attributes
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setAttribute("a_colour", colourBuffer);

	    // Update angle based on time
	    theta += speed * deltaTime; // Counterclockwise motion

	    // Calculate circular position
	    float x = radius * (float) Math.cos(theta);
	    float y = radius * (float) Math.sin(theta);

	    // Create model matrix
	    Matrix4f modelMatrix = new Matrix4f();
	    translationMatrix(x, y, modelMatrix); // Move along circular path
	    rotationMatrix(theta, modelMatrix); // Rotate to match direction

	    // Pass model matrix to shader
	    shader.setUniform("u_modelMatrix", modelMatrix);

	    // Draw using index buffer
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}



	/**
	 * Set the destination matrix to a translation matrix. Note the destination
	 * matrix must already be allocated.
	 * 
	 * @param tx   Offset in the x direction
	 * @param ty   Offset in the y direction
	 * @param dest Destination matrix to write into
	 * @return
	 */

	public static Matrix4f translationMatrix(float tx, float ty, Matrix4f dest) {
		// clear the matrix to the identity matrix
		dest.identity();

		//     [ 1 0 0 tx ]
		// T = [ 0 1 0 ty ]
	    //     [ 0 0 0 0  ]
		//     [ 0 0 0 1  ]

		// Perform operations on only the x and y values of the T vec. 
		// Leaves the z value alone, as we are only doing 2D transformations.
		
		dest.m30(tx);
		dest.m31(ty);

		return dest;
	}

	/**
	 * Set the destination matrix to a rotation matrix. Note the destination matrix
	 * must already be allocated.
	 *
	 * @param angle Angle of rotation (in radians)
	 * @param dest  Destination matrix to write into
	 * @return
	 */

	public static Matrix4f rotationMatrix(float angle, Matrix4f dest) {

		// TODO: Your code here
		dest.identity();
		
	    // Rotation matrix:
	    // R = [ cosθ  -sinθ  0  0 ]
	    //     [ sinθ   cosθ  0  0 ]
	    //     [  0      0    1  0 ]
	    //     [  0      0    0  1 ]
		
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		
		dest.m00(cos);
		dest.m01(-sin);
		dest.m10(sin);
		dest.m11(cos);

		return dest;
	}

	/**
	 * Set the destination matrix to a scale matrix. Note the destination matrix
	 * must already be allocated.
	 *
	 * @param sx   Scale factor in x direction
	 * @param sy   Scale factor in y direction
	 * @param dest Destination matrix to write into
	 * @return
	 */

	public static Matrix4f scaleMatrix(float sx, float sy, Matrix4f dest) {

		// TODO: Your code here
		dest.identity();
		
	    // Scaling matrix:
	    // S = [ sx  0  0  0 ]
	    //     [  0 sy  0  0 ]
	    //     [  0  0  1  0 ]
	    //     [  0  0  0  1 ]
		
		dest.m00(sx);
		dest.m11(sy);
		
		return dest;
	}
	
	

}
