package org.lwjgl.game;

import org.joml.Vector3f;
import org.lwjgl.engine.graph.Mesh;

public class GameItem {
    private final Mesh mesh;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public GameItem(Mesh mesh) {
        this.mesh = mesh;
        this.position = new Vector3f(0, 0, 0);
        this.scale = 1;
        this.rotation = new Vector3f(0, 0, 0);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public GameItem setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
