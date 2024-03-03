$version: "2.0"

namespace com.kmalik.smithy.preprocessing

use smithy4s.meta#packedInputs

@trait(selector: ":is(structure, structure > member)")
structure isTenantAware {}

@trait(selector: "service")
structure tenantAgnostic {}


@packedInputs
service MyTenantAwareService {
    version: "1.0.0",
    operations: [myRead, myWrite, myDelete]
}

@packedInputs
@tenantAgnostic
service MyTenantAgnosticService {
    version: "1.0.0",
    operations: [myRead, myWrite]
}

@packedInputs
@tenantAgnostic
service MyTenantAgnosticReaderService {
    version: "1.0.0",
    operations: [myRead]
}

operation myRead {
    input := {
        @required
        tenantContext: MyTenantContext

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
        tenantContext: MyTenantContext

        @isTenantAware
        tenantDetails: MyTenantDetails

        @required
        obj: MyObject
    }
    output := {
        @required
        success: Boolean
    }
}

operation myDelete {
    input := {
        @required
        tenantContext: MyTenantContext

        @required
        id: String
    },
    output: Unit
}

structure MyObject {
    @required
    id: String

    @required
    data: String
}

@isTenantAware
structure MyTenantContext {
    @required
    tenantId: String
}

structure MyTenantDetails {
    @required
    isProduction: Boolean

    @required
    tier: TENANT_TIER
}

enum TENANT_TIER {
    BASIC
    PREMIUM
}