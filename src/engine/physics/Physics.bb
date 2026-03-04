Include "src/engine/physics/CollisionTypes.bb"

Function Physics_Init()

    Collisions HIT_PLAYER, HIT_MAP, 2, 2
    Collisions HIT_PLAYER, HIT_PLAYER, 1, 3
    Collisions HIT_ITEM, HIT_MAP, 2, 2
    Collisions HIT_APACHE, HIT_APACHE, 1, 2
    Collisions HIT_178, HIT_MAP, 2, 2
    Collisions HIT_178, HIT_178, 1, 3
    Collisions HIT_DEAD, HIT_MAP, 2, 2

End Function