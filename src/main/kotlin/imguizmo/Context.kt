package imguizmo

import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import imgui.classes.DrawList

data class Context(
    @NativeType("ImDrawList*") var drawList: DrawList,

    val mode: ImGuizmo.MODE = ImGuizmo.MODE.NONE,
    @NativeType("matrix_t") val viewMat: Mat4 = Mat4(),
    @NativeType("matrix_t") val projectionMat: Mat4 = Mat4(),
    @NativeType("matrix_t") val model: Mat4 = Mat4(),
    @NativeType("matrix_t") val modelInverse: Mat4 = Mat4(),
    @NativeType("matrix_t") val modelSource: Mat4 = Mat4(),
    @NativeType("matrix_t") val modelSourceInverse: Mat4 = Mat4(),
    @NativeType("matrix_t") val mvp: Mat4 = Mat4(),
    @NativeType("matrix_t") val viewProjection: Mat4 = Mat4(),

    @NativeType("vec_t") val modelScaleOriginval: Vec3 = Vec3(),
    @NativeType("vec_t") val cameraEye: Vec3 = Vec3(),
    @NativeType("vec_t") val cameraRight: Vec3 = Vec3(),
    @NativeType("vec_t") val cameraDir: Vec3 = Vec3(),
    @NativeType("vec_t") val cameraUp: Vec3 = Vec3(),
    @NativeType("vec_t") val rayOrigin: Vec3 = Vec3(),
    @NativeType("vec_t") val rayVector: Vec3 = Vec3(),

    val radiusSquareCenter: Float = 0f,
    @NativeType("ImVec2") val screenSquareCenter: Vec2 = Vec2(),
    @NativeType("ImVec2") val screenSquareMin: Vec2 = Vec2(),
    @NativeType("ImVec2") val screenSquareMax: Vec2 = Vec2(),

    val screenFactor: Float = 0f,
    @NativeType("vec_t") val relativeOrigin: Vec3 = Vec3(),

    var using: Boolean = false,
    var enable: Boolean = true,

    // translation
    @NativeType("vec_t") val translationPlan: Vec3 = Vec3(),
    @NativeType("vec_t") val translationPlanOrigin: Vec3 = Vec3(),
    @NativeType("vec_t") val matrixOrigin: Vec3 = Vec3(),
    @NativeType("vec_t") val translationLastDelta: Vec3 = Vec3(),

    // rotation
    @NativeType("vec_t") val rotationVectorSource: Vec3 = Vec3(),
    val rotationAngle: Float = 0f,
    val rotationAngleOrigin: Float = 0f,
    //vec_t mWorldToLocalAxis;

    // scale
    @NativeType("vec_t") val scale: Vec3 = Vec3(),
    @NativeType("vec_t") val scaleValueOrigin: Vec3 = Vec3(),
    @NativeType("vec_t") val scaleLast: Vec3 = Vec3(),
    val saveMousePosx: Float = 0f,

    // save axis factor when using gizmo
    @ArraySize(3) val belowAxisLimit: BooleanArray = BooleanArray(3),
    @ArraySize(3) val belowPlaneLimit: BooleanArray = BooleanArray(3),
    @ArraySize(3) val axisFactor: FloatArray = FloatArray(3),

    // bounds stretching
    @NativeType("vec_t") val boundsPivot: Vec3 = Vec3(),
    @NativeType("vec_t") val boundsAnchor: Vec3 = Vec3(),
    @NativeType("vec_t") val boundsPlan: Vec3 = Vec3(),
    @NativeType("vec_t") val boundsLocalPivot: Vec3 = Vec3(),
    val boundsBestAxis: Int = 0,
    @ArraySize(2) val boundsAxis: IntArray = IntArray(2),
    var usingBounds: Boolean = false,
    @NativeType("matrix_t") val boundsMatrix: Mat4 = Mat4(),

    //
    val currentOperation: Int = 0,

    val x: Float = 0f,
    val y: Float = 0f,
    val width: Float = 0f,
    val height: Float = 0f,
    val xMax: Float = 0f,
    val yMax: Float = 0f,
    val displayRatio: Float = 1f,

    val isOrthographic: Boolean = false,

    val actualID: Int = -1,
    val editingID: Int = -1,
    val operation: ImGuizmo.OPERATION = ImGuizmo.OPERATION(-1),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Context

        if (drawList != other.drawList) return false
        if (mode != other.mode) return false
        if (viewMat != other.viewMat) return false
        if (projectionMat != other.projectionMat) return false
        if (model != other.model) return false
        if (modelInverse != other.modelInverse) return false
        if (modelSource != other.modelSource) return false
        if (modelSourceInverse != other.modelSourceInverse) return false
        if (mvp != other.mvp) return false
        if (viewProjection != other.viewProjection) return false
        if (modelScaleOriginval != other.modelScaleOriginval) return false
        if (cameraEye != other.cameraEye) return false
        if (cameraRight != other.cameraRight) return false
        if (cameraDir != other.cameraDir) return false
        if (cameraUp != other.cameraUp) return false
        if (rayOrigin != other.rayOrigin) return false
        if (rayVector != other.rayVector) return false
        if (radiusSquareCenter != other.radiusSquareCenter) return false
        if (screenSquareCenter != other.screenSquareCenter) return false
        if (screenSquareMin != other.screenSquareMin) return false
        if (screenSquareMax != other.screenSquareMax) return false
        if (screenFactor != other.screenFactor) return false
        if (relativeOrigin != other.relativeOrigin) return false
        if (using != other.using) return false
        if (enable != other.enable) return false
        if (translationPlan != other.translationPlan) return false
        if (translationPlanOrigin != other.translationPlanOrigin) return false
        if (matrixOrigin != other.matrixOrigin) return false
        if (translationLastDelta != other.translationLastDelta) return false
        if (rotationVectorSource != other.rotationVectorSource) return false
        if (rotationAngle != other.rotationAngle) return false
        if (rotationAngleOrigin != other.rotationAngleOrigin) return false
        if (scale != other.scale) return false
        if (scaleValueOrigin != other.scaleValueOrigin) return false
        if (scaleLast != other.scaleLast) return false
        if (saveMousePosx != other.saveMousePosx) return false
        if (!belowAxisLimit.contentEquals(other.belowAxisLimit)) return false
        if (!belowPlaneLimit.contentEquals(other.belowPlaneLimit)) return false
        if (!axisFactor.contentEquals(other.axisFactor)) return false
        if (boundsPivot != other.boundsPivot) return false
        if (boundsAnchor != other.boundsAnchor) return false
        if (boundsPlan != other.boundsPlan) return false
        if (boundsLocalPivot != other.boundsLocalPivot) return false
        if (boundsBestAxis != other.boundsBestAxis) return false
        if (!boundsAxis.contentEquals(other.boundsAxis)) return false
        if (usingBounds != other.usingBounds) return false
        if (boundsMatrix != other.boundsMatrix) return false
        if (currentOperation != other.currentOperation) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (xMax != other.xMax) return false
        if (yMax != other.yMax) return false
        if (displayRatio != other.displayRatio) return false
        if (isOrthographic != other.isOrthographic) return false
        if (actualID != other.actualID) return false
        if (editingID != other.editingID) return false
        if (operation != other.operation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = drawList.hashCode()
        result = 31 * result + mode.hashCode()
        result = 31 * result + viewMat.hashCode()
        result = 31 * result + projectionMat.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + modelInverse.hashCode()
        result = 31 * result + modelSource.hashCode()
        result = 31 * result + modelSourceInverse.hashCode()
        result = 31 * result + mvp.hashCode()
        result = 31 * result + viewProjection.hashCode()
        result = 31 * result + modelScaleOriginval.hashCode()
        result = 31 * result + cameraEye.hashCode()
        result = 31 * result + cameraRight.hashCode()
        result = 31 * result + cameraDir.hashCode()
        result = 31 * result + cameraUp.hashCode()
        result = 31 * result + rayOrigin.hashCode()
        result = 31 * result + rayVector.hashCode()
        result = 31 * result + radiusSquareCenter.hashCode()
        result = 31 * result + screenSquareCenter.hashCode()
        result = 31 * result + screenSquareMin.hashCode()
        result = 31 * result + screenSquareMax.hashCode()
        result = 31 * result + screenFactor.hashCode()
        result = 31 * result + relativeOrigin.hashCode()
        result = 31 * result + using.hashCode()
        result = 31 * result + enable.hashCode()
        result = 31 * result + translationPlan.hashCode()
        result = 31 * result + translationPlanOrigin.hashCode()
        result = 31 * result + matrixOrigin.hashCode()
        result = 31 * result + translationLastDelta.hashCode()
        result = 31 * result + rotationVectorSource.hashCode()
        result = 31 * result + rotationAngle.hashCode()
        result = 31 * result + rotationAngleOrigin.hashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + scaleValueOrigin.hashCode()
        result = 31 * result + scaleLast.hashCode()
        result = 31 * result + saveMousePosx.hashCode()
        result = 31 * result + belowAxisLimit.contentHashCode()
        result = 31 * result + belowPlaneLimit.contentHashCode()
        result = 31 * result + axisFactor.contentHashCode()
        result = 31 * result + boundsPivot.hashCode()
        result = 31 * result + boundsAnchor.hashCode()
        result = 31 * result + boundsPlan.hashCode()
        result = 31 * result + boundsLocalPivot.hashCode()
        result = 31 * result + boundsBestAxis
        result = 31 * result + boundsAxis.contentHashCode()
        result = 31 * result + usingBounds.hashCode()
        result = 31 * result + boundsMatrix.hashCode()
        result = 31 * result + currentOperation
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + xMax.hashCode()
        result = 31 * result + yMax.hashCode()
        result = 31 * result + displayRatio.hashCode()
        result = 31 * result + isOrthographic.hashCode()
        result = 31 * result + actualID
        result = 31 * result + editingID
        result = 31 * result + operation.hashCode()
        return result
    }
}
