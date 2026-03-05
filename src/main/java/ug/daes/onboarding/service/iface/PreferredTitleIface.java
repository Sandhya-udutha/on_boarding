package ug.daes.onboarding.service.iface;

import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.TitleDto;

public interface PreferredTitleIface {

    ApiResponse getPreferredTitles();

    ApiResponse addUpdateTitle(TitleDto titleDto);
}
