package com.gplio.fibrewallpaper.lib.graphics

import android.opengl.Matrix

fun matrix4(): FloatArray? {
    return FloatArray(16)
}

fun lookAt(matrix: FloatArray?, eye: Vec3<Float>, center: Vec3<Float>, up: Vec3<Float>) {
    Matrix.setLookAtM(
        matrix,
        0,
        eye.x!!, eye.y!!, eye.z!!,
        center.x!!, center.y!!, center.z!!,
        up.x!!, up.y!!, up.z!!
    )
}

class Vec3<T> {
    var x: T
    var y: T
    var z: T

    constructor(x: T, y: T, z: T) {
        this.x = x
        this.y = y
        this.z = z
    }

    internal constructor(`val`: T) {
        x = `val`
        y = `val`
        z = `val`
    }
}

class v4<T> {
    var x: T? = null
    var y: T? = null
    var z: T? = null
    var w: T? = null
}