Global Mesh_MinX#, Mesh_MinY#, Mesh_MinZ#
Global Mesh_MaxX#, Mesh_MaxY#, Mesh_MaxZ#
Global Mesh_MagX#, Mesh_MagY#, Mesh_MagZ#
; Create a collision box For a mesh entity taking into account entity scale
; (will not work in non-uniform scaled space)
Function MakeCollBox(mesh%)
	Local sx# = GetMeshScaleX(mesh, 1)
	Local sy# = Max(GetMeshScaleY(mesh, 1), 0.001)
	Local sz# = GetMeshScaleZ(mesh, 1)
	GetMeshExtents(mesh)
	EntityBox mesh, Mesh_MinX * sx, Mesh_MinY * sy, Mesh_MinZ * sz, Mesh_MagX * sx, Mesh_MagY * sy, Mesh_MagZ * sz
End Function

Function GetMeshExtents(Mesh%)
	Local s%, surf%, surfs%, v%, verts%, x#, y#, z#
	Local minx# = INFINITY
	Local miny# = INFINITY
	Local minz# = INFINITY
	Local maxx# = -INFINITY
	Local maxy# = -INFINITY
	Local maxz# = -INFINITY
	
	surfs = CountSurfaces(Mesh)
	
	For s = 1 To surfs
		surf = GetSurface(Mesh, s)
		verts = CountVertices(surf)
		
		For v = 0 To verts - 1
			x = VertexX(surf, v)
			y = VertexY(surf, v)
			z = VertexZ(surf, v)
			
			If (x < minx) Then minx = x
			If (x > maxx) Then maxx = x
			If (y < miny) Then miny = y
			If (y > maxy) Then maxy = y
			If (z < minz) Then minz = z
			If (z > maxz) Then maxz = z
		Next
	Next
	
	Mesh_MinX = minx
	Mesh_MinY = miny
	Mesh_MinZ = minz
	Mesh_MaxX = maxx
	Mesh_MaxY = maxy
	Mesh_MaxZ = maxz
	Mesh_MagX = maxx-minx
	Mesh_MagY = maxy-miny
	Mesh_MagZ = maxz-minz
	
End Function

Function GetMeshScaleX#(entity%, globl% = False)
	If globl Then TFormVector 1, 0, 0, entity, 0 Else TFormVector 1, 0, 0, entity, GetParent(entity)
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function 

Function GetMeshScaleY#(entity%, globl% = False)
	If globl Then TFormVector 0, 1, 0, entity, 0 Else TFormVector 0, 1, 0, entity, GetParent(entity)
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function 

Function GetMeshScaleZ#(entity%, globl% = False)
	If globl Then TFormVector 0, 0, 1, entity, 0 Else TFormVector 0, 0, 1, entity, GetParent(entity)
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function 