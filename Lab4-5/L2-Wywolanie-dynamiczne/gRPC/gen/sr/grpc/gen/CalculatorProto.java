// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calculator.proto

package sr.grpc.gen;

public final class CalculatorProto {
  private CalculatorProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_A_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_A_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_Numbers_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_Numbers_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_AddRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_AddRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_AddResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_AddResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_SubtractRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_SubtractRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_SubtractResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_SubtractResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_OpRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_OpRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_OpResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_OpResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_AvgRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_AvgRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_AvgResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_AvgResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020calculator.proto\022\ncalculator\"/\n\001A\022\t\n\001a" +
      "\030\001 \001(\003\022\t\n\001b\030\002 \001(\003\022\t\n\001c\030\003 \001(\002\022\t\n\001d\030\004 \001(\t\"" +
      "\032\n\007Numbers\022\017\n\007numbers\030\001 \003(\003\"\"\n\nAddReques" +
      "t\022\t\n\001a\030\001 \001(\003\022\t\n\001b\030\002 \001(\003\"\035\n\013AddResponse\022\016" +
      "\n\006result\030\001 \001(\003\"\'\n\017SubtractRequest\022\t\n\001a\030\001" +
      " \001(\003\022\t\n\001b\030\002 \001(\003\"\"\n\020SubtractResponse\022\016\n\006r" +
      "esult\030\001 \001(\003\"2\n\tOpRequest\022\031\n\002a1\030\001 \001(\0132\r.c" +
      "alculator.A\022\n\n\002b1\030\002 \001(\003\"\014\n\nOpResponse\",\n" +
      "\nAvgRequest\022\036\n\001n\030\001 \001(\0132\023.calculator.Numb" +
      "ers\"\035\n\013AvgResponse\022\016\n\006result\030\001 \001(\002*&\n\tOp" +
      "eration\022\007\n\003MIN\020\000\022\007\n\003MAX\020\001\022\007\n\003AVG\020\0022\200\002\n\nC" +
      "alculator\0228\n\003Add\022\026.calculator.AddRequest" +
      "\032\027.calculator.AddResponse\"\000\022G\n\010Subtract\022" +
      "\033.calculator.SubtractRequest\032\034.calculato" +
      "r.SubtractResponse\"\000\0225\n\002Op\022\025.calculator." +
      "OpRequest\032\026.calculator.OpResponse\"\000\0228\n\003A" +
      "vg\022\026.calculator.AvgRequest\032\027.calculator." +
      "AvgResponse\"\000B \n\013sr.grpc.genB\017Calculator" +
      "ProtoP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_calculator_A_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_calculator_A_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_A_descriptor,
        new java.lang.String[] { "A", "B", "C", "D", });
    internal_static_calculator_Numbers_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_calculator_Numbers_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_Numbers_descriptor,
        new java.lang.String[] { "Numbers", });
    internal_static_calculator_AddRequest_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_calculator_AddRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_AddRequest_descriptor,
        new java.lang.String[] { "A", "B", });
    internal_static_calculator_AddResponse_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_calculator_AddResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_AddResponse_descriptor,
        new java.lang.String[] { "Result", });
    internal_static_calculator_SubtractRequest_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_calculator_SubtractRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_SubtractRequest_descriptor,
        new java.lang.String[] { "A", "B", });
    internal_static_calculator_SubtractResponse_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_calculator_SubtractResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_SubtractResponse_descriptor,
        new java.lang.String[] { "Result", });
    internal_static_calculator_OpRequest_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_calculator_OpRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_OpRequest_descriptor,
        new java.lang.String[] { "A1", "B1", });
    internal_static_calculator_OpResponse_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_calculator_OpResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_OpResponse_descriptor,
        new java.lang.String[] { });
    internal_static_calculator_AvgRequest_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_calculator_AvgRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_AvgRequest_descriptor,
        new java.lang.String[] { "N", });
    internal_static_calculator_AvgResponse_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_calculator_AvgResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_AvgResponse_descriptor,
        new java.lang.String[] { "Result", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
