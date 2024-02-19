$version: "2.0"

namespace com.kmalik.smithy.service

use smithy4s.meta#packedInputs

@packedInputs
service MyService {
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
}
