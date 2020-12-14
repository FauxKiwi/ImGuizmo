package imguizmo

import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import imgui.classes.Context
import imgui.classes.DrawList

/**
 * call inside your own window and before Manipulate() in order to draw gizmo to that window.
 * Or pass a specific ImDrawList to draw to (e.g. ImGui::GetForegroundDrawList()).
 */
inline fun setDrawlist(drawlist: DrawList? = null) = ImGuizmo.setDrawlist(drawlist)

/**
 * call BeginFrame right after ImGui_XXXX_NewFrame();
 */
inline fun beginFrame() = ImGuizmo.beginFrame()

/**
 * this is necessary because when imguizmo is compiled into a dll, and imgui into another
 * globals are not shared between them.
 * More details at https://stackoverflow.com/questions/19373061/what-happens-to-global-and-static-variables-in-a-shared-library-when-it-is-dynam
 * expose method to set imgui context
 */
inline fun setImGuiContext(ctx: Context) = ImGuizmo.setImGuiContext(ctx)

/**
 * return true if mouse cursor is over any gizmo control (axis, plan or screen component)
 */
inline fun isOver(): Boolean = ImGuizmo.isOver()

/**
 * return true if mouse IsOver or if the gizmo is in moving state
 */
inline fun isUsing(): Boolean = ImGuizmo.isUsing()

/**
 * enable/disable the gizmo. Stay in the state until next call to Enable.
 * gizmo is rendered with gray half transparent color when disabled
 */
inline fun enable(enable: Boolean) = ImGuizmo.enable(enable)

/**
 * helper functions for manually editing translation/rotation/scale with an input float
 * translation, rotation and scale float points to 3 floats each
 * Angles are in degrees (more suitable for human editing)
 * example:
 * float matrixTranslation[3], matrixRotation[3], matrixScale[3];
 * ImGuizmo::DecomposeMatrixToComponents(gizmoMatrix.m16, matrixTranslation, matrixRotation, matrixScale);
 * ImGui::InputFloat3("Tr", matrixTranslation, 3);
 * ImGui::InputFloat3("Rt", matrixRotation, 3);
 * ImGui::InputFloat3("Sc", matrixScale, 3);
 * ImGuizmo::RecomposeMatrixFromComponents(matrixTranslation, matrixRotation, matrixScale, gizmoMatrix.m16);
 *
 * These functions have some numerical stability issues for now. Use with caution.
 */
inline fun decomposeMatrixToComponents(
    matrix: Mat4, translation: Vec3, rotation: Vec3, scale: Vec3
) = ImGuizmo.decomposeMatrixToComponents(matrix, translation, rotation, scale)
inline fun recomposeMatrixFromComponents(
    translation: Vec3, rotation: Vec3, scale: Vec3, matrix: Mat4
)  = ImGuizmo.recomposeMatrixFromComponents(translation, rotation, scale, matrix)

inline fun setRect(x: Float, y: Float, width: Float, height: Float) = ImGuizmo.setRect(x, y, width, height)
/**
 * default is false
 */
inline fun setOrthographic(isOrthographic: Boolean) = ImGuizmo.setOrthographic(isOrthographic)

/**
 * Render a cube with face color corresponding to face normal. Useful for debug/tests
 */
inline fun drawCubes(
    view: Mat4, projection: Mat4, matrices: Array<Mat4>/*, int matrixCount*/
) = ImGuizmo.drawCubes(view, projection, matrices)
inline fun drawGrid(
    view: Mat4, projection: Mat4, matrix: Mat4, gridSize: Float
) = ImGuizmo.drawGrid(view, projection, matrix, gridSize)

/**
 * call it when you want a gizmo
 * Needs view and projection matrices.
 * matrix parameter is the source matrix (where will be gizmo be drawn) and might be transformed by the function. Return deltaMatrix is optional
 * translation is applied in world space
 */
 enum class OPERATION {
    NONE,
    TRANSLATE,
    ROTATE,
    SCALE,
    BOUNDS;
    companion object {
        operator fun invoke(i: Int = 0) = values()[i+1]
    }
}

enum class MODE {
    NONE,
    LOCAL,
    WORLD;
    companion object {
        operator fun invoke(i: Int = 0) = values()[i+1]
    }
}

inline fun manipulate(
    view: Mat4, projection: Mat4, operation: OPERATION, mode: MODE, matrix: Mat4, deltaMatrix: Mat4? = null, snap: Vec3? = null, localBounds: Vec3? = null, boundsSnap: Vec3? = null
): Boolean = ImGuizmo.manipulate(view, projection, operation, mode, matrix, deltaMatrix, snap, localBounds, boundsSnap)

/**
 * Please note that this cubeview is patented by Autodesk : https://patents.google.com/patent/US7782319B2/en
 * It seems to be a defensive patent in the US. I don't think it will bring troubles using it as
 * other software are using the same mechanics. But just in case, you are now warned!
 */
inline fun viewManipulate(
    view: Mat4, length: Float, position: Vec2, size: Vec2, backgroundColor: Vec4
) = ImGuizmo.viewManipulate(view, length, position, size, backgroundColor)

inline fun setID(id: Int) = ImGuizmo.setID(id)

/**
 * return true if the cursor is over the operation's gizmo
 */
inline fun isOver(op: OPERATION): Boolean = ImGuizmo.isOver(op)
inline fun setGizmoSizeClipSpace(value: Float) = ImGuizmo.setGizmoSizeClipSpace(value)