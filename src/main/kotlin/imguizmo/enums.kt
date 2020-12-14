package imguizmo

enum class MOVETYPE {
    NONE,
    MOVE_X,
    MOVE_Y,
    MOVE_Z,
    MOVE_YZ,
    MOVE_ZX,
    MOVE_XY,
    MOVE_SCREEN,
    ROTATE_X,
    ROTATE_Y,
    ROTATE_Z,
    ROTATE_SCREEN,
    SCALE_X,
    SCALE_Y,
    SCALE_Z,
    SCALE_XYZ;
    companion object {
        operator fun invoke(i: Int = 0) = values()[i+1]
    }
}