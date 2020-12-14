package imguizmo

import glm_.vec3.Vec3
import glm_.vec4.Vec4

infix fun Vec3.assign(other: Vec3) {
    x = other.x
    y = other.y
    z = other.z
}

infix fun Vec3.assign(other: Vec4) {
    x = other.x
    y = other.y
    z = other.z
}