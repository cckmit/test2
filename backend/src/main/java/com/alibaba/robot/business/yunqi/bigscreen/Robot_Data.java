// automatically generated by the FlatBuffers compiler, do not modify

package com.alibaba.robot.business.yunqi.bigscreen;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.alibaba.robot.flatbuffers.*;

@SuppressWarnings("unused")
public final class Robot_Data extends Table {
  public static Robot_Data getRootAsRobot_Data(ByteBuffer _bb) { return getRootAsRobot_Data(_bb, new Robot_Data()); }
  public static Robot_Data getRootAsRobot_Data(ByteBuffer _bb, Robot_Data obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public Robot_Data __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public float x() { int o = __offset(4); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float y() { int o = __offset(6); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float angle() { int o = __offset(8); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public float odom() { int o = __offset(10); return o != 0 ? bb.getFloat(o + bb_pos) : 0.0f; }
  public int total() { int o = __offset(12); return o != 0 ? bb.getShort(o + bb_pos) & 0xFFFF : 0; }
  public int status() { int o = __offset(14); return o != 0 ? bb.getShort(o + bb_pos) & 0xFFFF : 0; }
  public int oprTimes() { int o = __offset(16); return o != 0 ? bb.getShort(o + bb_pos) & 0xFFFF : 0; }

  public static int createRobot_Data(FlatBufferBuilder builder,
      float x,
      float y,
      float angle,
      float odom,
      int total,
      int status,
      int opr_times) {
    builder.startObject(7);
    Robot_Data.addOdom(builder, odom);
    Robot_Data.addAngle(builder, angle);
    Robot_Data.addY(builder, y);
    Robot_Data.addX(builder, x);
    Robot_Data.addOprTimes(builder, opr_times);
    Robot_Data.addStatus(builder, status);
    Robot_Data.addTotal(builder, total);
    return Robot_Data.endRobot_Data(builder);
  }

  public static void startRobot_Data(FlatBufferBuilder builder) { builder.startObject(7); }
  public static void addX(FlatBufferBuilder builder, float x) { builder.addFloat(0, x, 0.0f); }
  public static void addY(FlatBufferBuilder builder, float y) { builder.addFloat(1, y, 0.0f); }
  public static void addAngle(FlatBufferBuilder builder, float angle) { builder.addFloat(2, angle, 0.0f); }
  public static void addOdom(FlatBufferBuilder builder, float odom) { builder.addFloat(3, odom, 0.0f); }
  public static void addTotal(FlatBufferBuilder builder, int total) { builder.addShort(4, (short)total, (short)0); }
  public static void addStatus(FlatBufferBuilder builder, int status) { builder.addShort(5, (short)status, (short)0); }
  public static void addOprTimes(FlatBufferBuilder builder, int oprTimes) { builder.addShort(6, (short)oprTimes, (short)0); }
  public static int endRobot_Data(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}
