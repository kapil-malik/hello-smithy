$version: "2.0"

namespace com.kmalik.smithy.refinement

use smithy4s.meta#packedInputs
use smithy4s.meta#refinement

@trait(selector: ":test(member > string)")
structure conflictResolverTag {}

apply conflictResolverTag @refinement(
    targetType: "com.kmalik.smithy.refinement.external.ConflictResolver"
)

@packedInputs
service MyServiceWithRefinement {
    version: "1.0.0",
    operations: [myRead, myWrite]
}

operation myRead {
    input := {
        @required
        id: String
    },
    output := {
        @required
        value: MyObject
    }
}

operation myWrite {
    input := {
        @required
        obj: MyObject

        @conflictResolverTag
        conflictResolver: String
    }
    output := {
        @required
        success: Boolean
    }
}

structure MyObject {
    @required
    id: String

    @required
    data: String

    @required
    version: Integer

    @required
    @timestampFormat("epoch-seconds")
    ts: Timestamp
}
