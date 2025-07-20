package arch.attanake.util;

import org.apache.avro.Schema;
import org.apache.avro.SchemaCompatibility;

public class AvroCompatibilityChecker {
    public static boolean checkCompatibility(Schema oldSchema, Schema newSchema) {
        SchemaCompatibility.SchemaPairCompatibility result =
                SchemaCompatibility.checkReaderWriterCompatibility(oldSchema, newSchema);

        return result.getType() == SchemaCompatibility.SchemaCompatibilityType.COMPATIBLE;
    }
}
