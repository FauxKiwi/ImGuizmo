package imguizmo

import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import imgui.Col
import imgui.ImGui
import imgui.StyleVar
import imgui.WindowFlag
import imgui.classes.Context
import imgui.classes.DrawList

object ImGuizmo {
    val context: imguizmo.Context = imguizmo.Context(ImGui.windowDrawList)

    val angleLimit = 0.96f
    val planeLimit = 0.2f

    @NativeType("const vec_t[]") val directionUnary: Array<Vec3> = arrayOf(
        Vec3(1f, 0f, 0f),
        Vec3(0f, 1f, 0f),
        Vec3(0f, 0f, 1f)
    )
    @NativeType("const ImU32[]") val directionColor: Array<Vec4> = arrayOf(
        Vec4(0f, 0f, 0.667f, 1f)/*0xFF0000AA*/,
        Vec4(0f, 0.667f, 0f, 1f)/*0xFF00AA00*/,
        Vec4(0.667f, 0f, 0f, 1f)/*0xFFAA0000*/
    )

// Alpha: 100%: FF, 87%: DE, 70%: B3, 54%: 8A, 50%: 80, 38%: 61, 12%: 1F
    @NativeType("const ImU32[]") val planeColor: Array<Vec4> = arrayOf(
        Vec4(0f, 0f, 0.667f, 0.380f)/*0x610000AA*/,
        Vec4(0f, 0.667f, 0f, 0.380f)/*0x6100AA00*/,
        Vec4(0.667f, 0f, 0f, 0.380f)/*0x61AA0000*/
    )
    @NativeType("const ImU32") val selectionColor = Vec4(0.063f, 0.502f, 1f, 0.541f)//0x8A1080FF
    @NativeType("const ImU32") val inactiveColor = Vec4(0.6f, 0.6f, 0.6f, 0.6f)//0x99999999
    @NativeType("const ImU32") val translationLineColor = Vec4(0.667f, 0.667f, 0.667f, 0.667f)//0xAAAAAAAA
    @NativeType("const char*[]") val translationInfoMask: Array<String> = arrayOf(
        "X : %5.3f", "Y : %5.3f", "Z : %5.3f",
        "Y : %5.3f Z : %5.3f", "X : %5.3f Z : %5.3f", "X : %5.3f Y : %5.3f",
        "X : %5.3f Y : %5.3f Z : %5.3f")
    @NativeType("const char*[]") val scaleInfoMask: Array<String> = arrayOf("X : %5.2f", "Y : %5.2f", "Z : %5.2f", "XYZ : %5.2f")
    @NativeType("static const char*[]") val rotationInfoMask: Array<String> = arrayOf(
    "X : %5.2f deg %5.2f rad", "Y : %5.2f deg %5.2f rad", "Z : %5.2f deg %5.2f rad","Screen : %5.2f deg %5.2f rad")
    val translationInfoIndex = intArrayOf(0,0,0, 1,0,0, 2,0,0, 1,2,0, 0,2,0, 0,1,0, 0,1,2)
    const val quadMin = 0.5f;
    const val quadMax = 0.8f;
    val quadUV = floatArrayOf(quadMin, quadMin, quadMin, quadMax, quadMax, quadMax, quadMax, quadMin)
    const val halfCircleSegmentCount = 64
    const val snapTension = 0.5f

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
    fun getMoveType(@NativeType("vec_t*") gizmoHitProportion: Vec3): Int {
        TODO()
    }
    val rotateType: Int get() {
        TODO()
    }
    val scaleType: Int get() {
        TODO()
    }

    @NativeType("ImVec2") fun worldToPos(
        @NativeType("const vec_t&") worldPos: Vec3,
        @NativeType("const matrix_t&") mat: Mat4,
        @NativeType("ImVec2") position: Vec2 = Vec2(context.x, context.y),
        @NativeType("ImVec2") size: Vec2 = Vec2(context.width, context.height)
    ): Vec2 {
        //vec_t trans;
        //trans.TransformPoint(worldPos, mat);
        val trans = mat * Vec4(worldPos, 1f)
        trans *= 0.5f / trans.w
        trans += Vec4(0.5f, 0.5f, 0f, 0f)
        trans.y = 1f - trans.y
        trans.x *= size.x
        trans.y *= size.y
        trans.x += position.x
        trans.y += position.y
        return Vec2(trans.x, trans.y)
    }

    fun computeCameraRay(
        @NativeType("vec_t&") rayOrigin: Vec3,
        @NativeType("vec_t&") rayDir: Vec3,
        @NativeType("ImVec2") position: Vec2 = Vec2(context.x, context.y),
        @NativeType("ImVec2") size: Vec2 = Vec2(context.width, context.height)
    ) {
        val io = ImGui.io //ImGuiIO& io = ImGui::GetIO();

        //matrix_t mViewProjInverse;
        //mViewProjInverse.Inverse(gContext.mViewMat * gContext.mProjectionMat);
        val viewProjInverse = Mat4()
        (context.viewMat * context.projectionMat).inverse(viewProjInverse)

        val mox = ((io.mousePos.x - position.x) / size.x) * 2f - 1f
        val moy = (1f - ((io.mousePos.y - position.y) / size.y)) * 2f - 1f

        //rayOrigin.Transform(makeVect(mox, moy, 0.f, 1.f), mViewProjInverse);
        val _rayOrigin = (viewProjInverse * Vec4(mox, moy, 0f, 0f))
        rayOrigin assign _rayOrigin
        rayOrigin *= 1f / _rayOrigin.w
        //vec_t rayEnd;
        //rayEnd.Transform(makeVect(mox, moy, 1.f - FLT_EPSILON, 1.f), mViewProjInverse);
        val rayEnd = viewProjInverse * Vec4(mox, moy, 1f - FLT_EPSILON, 1f)
        rayEnd *= 1f / rayEnd.w
        val _rayDir =(Vec3(rayEnd) - rayOrigin)
        rayDir assign _rayDir
    }




    ///////////////////////////////////////////////////////////////////////////
    // ImGuizmo API
    ///////////////////////////////////////////////////////////////////////////

    fun setDrawlist(@NativeType("ImDrawList*") drawlist: DrawList? = null) {
        context.drawList = drawlist ?: ImGui.windowDrawList
    }

    fun beginFrame() {
        val io = ImGui.io

        val flags = WindowFlag.NoTitleBar.i or
                    WindowFlag.NoResize.i or
                    WindowFlag.NoScrollbar.i or
                    WindowFlag.NoInputs.i or
                    WindowFlag.NoSavedSettings.i or
                    WindowFlag.NoFocusOnAppearing.i or
                    WindowFlag.NoBringToFrontOnFocus.i

        //if (IMGUI_HAS_VIEWPORT) { //#ifdef IMGUI_HAS_VIEWPORT
        /*ImGui.setNextWindowSize(ImGui.getMainViewport().size);
        ImGui.setNextWindowPos(ImGui::GetMainViewport().pos);*/
        //else { //#else
        ImGui.setNextWindowSize(Vec2(io.displaySize))
        ImGui.setNextWindowPos(Vec2(0, 0))
        //} //#endif

        ImGui.pushStyleColor(Col.WindowBg, Vec4(0f, 0f, 0f, 0f))
        ImGui.pushStyleColor(Col.Border, Vec4(0f, 0f, 0f, 0f))
        ImGui.pushStyleVar(StyleVar.WindowRounding, 0f)

        ImGui.begin("gizmo", null, flags)
        context.drawList = ImGui.windowDrawList
        ImGui.end()
        ImGui.popStyleVar()
        ImGui.popStyleColor(2)
    }

    fun setImGuiContext(@NativeType("ImGuiContext*") ctx: Context) {
        ctx.setCurrent()
    }

    fun isOver(): Boolean {
        return (context.operation == OPERATION.TRANSLATE && getMoveType(null) != MOVETYPE.NONE) ||
                (context.operation == OPERATION.ROTATE && getRotateType() != MOVETYPE.NONE) ||
                (context.operation == OPERATION.SCALE && getScaleType() != MOVETYPE.NONE) || isUsing();
    }

    fun isUsing(): Boolean {
        return context.using || context.usingBounds
    }

    fun enable(enable: Boolean) {
        context.enable = enable
        if (!enable) {
            context.using = false
            context.usingBounds = false
        }
    }

    fun decomposeMatrixToComponents(
        @NativeType("const float*") matrix: Mat4,
        @NativeType("float*") translation: Vec3,
        @NativeType("float*") rotation: Vec3,
        @NativeType("float*") scale: Vec3
    ) {
        TODO()
    }
    fun recomposeMatrixFromComponents(
        @NativeType("const float*") translation: Vec3,
        @NativeType("const float*") rotation: Vec3,
        @NativeType("const float*") scale: Vec3,
        @NativeType("float*") matrix: Mat4
    ) {
        TODO()
    }

    fun setRect(x: Float, y: Float, width: Float, height: Float) {
        TODO()
    }
    fun setOrthographic(isOrthographic: Boolean) {
        TODO()
    }

    fun drawCubes(
        @NativeType("const float*") view: Mat4,
        @NativeType("const float*") projection: Mat4,
        @NativeType("const float*") matrices: Array<Mat4>/*, int matrixCount*/
    ) {
        TODO()
    }
    fun drawGrid(
        @NativeType("const float*") view: Mat4,
        @NativeType("const float*") projection: Mat4,
        @NativeType("const float*") matrix: Mat4,
        @NativeType("const float") gridSize: Float
    ) {
        TODO()
    }

    fun manipulate(
        @NativeType("const float*") view: Mat4,
        @NativeType("const float*") projection: Mat4,
        operation: OPERATION,
        mode: MODE,
        @NativeType("float*") matrix: Mat4,
        @NativeType("float*") deltaMatrix: Mat4? = null,
        @NativeType("const float*") snap: Vec3? = null,
        @NativeType("const float*") localBounds: Vec3? = null,
        @NativeType("const float*") boundsSnap: Vec3? = null
    ): Boolean {
        TODO()
    }

    fun viewManipulate(
        @NativeType("float*") view: Mat4,
        length: Float,
        @NativeType("ImVec2") position: Vec2,
        @NativeType("ImVec2") size: Vec2,
        @NativeType("ImU32") backgroundColor: Vec4
    ) {
        TODO()
    }

    fun setID(id: Int) {

    }

    fun isOver(op: OPERATION): Boolean {
        TODO()
    }
    fun setGizmoSizeClipSpace(value: Float) {
        TODO()
    }
};
