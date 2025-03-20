package comp3170.week3;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL41.*;

import java.io.File;
import java.io.IOException;

import comp3170.OpenGLException;
import comp3170.IWindowListener;
import comp3170.Shader;
import comp3170.ShaderLibrary;
import comp3170.Window;

public class Week3 implements IWindowListener {

    private Window window;
    private Shader shader;

    final private File DIRECTORY = new File("src/comp3170/week3"); 

    private int width = 800;
    private int height = 800;
    private Scene scene;

    private long lastFrameTime;  // Stores the previous frame time
    private float deltaTime;      // Time elapsed between frames

    public Week3() throws OpenGLException {
        // Create window with title, size, and a listener (this)
        window = new Window("Week 3 prac", width, height, this);

        // Set the window as resizable
        window.setResizable(true);
        
        // Initialize time tracking
        lastFrameTime = System.nanoTime();

        // Start running the window
        window.run();
    }

    @Override
    public void init() {
        new ShaderLibrary(DIRECTORY);
        // Set the background colour to white
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    

        // Create the scene
        scene = new Scene();
    }

    @Override
    public void draw() {
        // Clear the colour buffer
        glClear(GL_COLOR_BUFFER_BIT);    

        long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f;
        lastFrameTime = currentTime;

        scene.draw(deltaTime);
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        glViewport(0, 0, width, height);
    }

    @Override
    public void close() {
        // Nothing needed here for now
    }

    public static void main(String[] args) throws IOException, OpenGLException {
        new Week3();
    }
}
