public class AprilTagReading {
    public final double distance;
    public final double bearing;
    public final double pitch;
    public final int id;
    final public double roll;
    final public double yaw;
    final public double x;
    final public double y;
    final public double z;

    public AprilTagReading(int id, double distance, double bearing, double pitch, double roll, double yaw, double x, double y, double z) {
        this.id = id;
        this.distance = distance;
        this.bearing = bearing;
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}