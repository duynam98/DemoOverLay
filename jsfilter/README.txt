Gọi hàm sau 1 khởi tạo FilterManager trước khi dùng:
	FilterManager.init(this);


FilterManager.getInstance().types : danh sách các magicFilterType(magicFilterType)


FilterManager.getInstance().getFilter(magicFilterType) : get GPUImageFilter by magicFilterType

FilterManager.getInstance().getFilterName(magicFilterType) : get Filter Name by magicFilterType