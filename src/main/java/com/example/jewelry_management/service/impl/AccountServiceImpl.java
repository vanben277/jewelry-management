package com.example.jewelry_management.service.impl;

import com.example.jewelry_management.dto.res.AccountResponse;
import com.example.jewelry_management.dto.res.LoginResponse;
import com.example.jewelry_management.dto.res.RegisterResponse;
import com.example.jewelry_management.dto.res.UpdateInfoAccountResponse;
import com.example.jewelry_management.enums.AccountGender;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.AccountStatus;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.NotFoundException;
import com.example.jewelry_management.form.*;
import com.example.jewelry_management.model.Account;
import com.example.jewelry_management.repository.AccountRepository;
import com.example.jewelry_management.service.AccountService;
import com.example.jewelry_management.service.EmailService;
import com.example.jewelry_management.service.FileStorageService;
import com.example.jewelry_management.utils.AccountValidatorUtils;
import com.example.jewelry_management.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final AccountValidatorUtils accountValidatorUtils;
    private final AccountValidator accountValidator;
    private final EmailService emailService;


    @Override
    @Transactional
    public RegisterResponse register(RegisterForm form) {

        accountValidator.validateUniqueEmail(form.getEmail());
        accountValidator.validateUniqueUserName(form.getUserName());
        accountValidator.validateUniquePhone(form.getPhone());

        var plainTextPassword = form.getPassword();
        form.setPassword(passwordEncoder.encode(plainTextPassword));

        Account account = new Account();
        modelMapper.map(form, account);

        MultipartFile avatarFile = form.getAvatar();
        String avatarUrl = null;
        if (avatarFile != null && !avatarFile.isEmpty()) {
            if (!fileStorageService.isValidImage(avatarFile)) {
                throw new BusinessException("Định dạng ảnh avatar không hợp lệ!", ErrorCodeConstant.INVALID_IMAGE);
            }
            if (avatarFile.getSize() > 2 * 1024 * 1024) {
                throw new BusinessException("Kích thước ảnh vượt quá giới hạn!", ErrorCodeConstant.IMAGE_TOO_LARGE);
            }
            avatarUrl = fileStorageService.storeImage(avatarFile, "users");
            if (avatarUrl == null) {
                throw new BusinessException("Không thể lưu ảnh mới!", ErrorCodeConstant.UPLOAD_FAILED);
            }
        }
        account.setAvatar(avatarUrl);

        Account saved = accountRepository.save(account);
        return modelMapper.map(saved, RegisterResponse.class);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginForm form) {
        Account account = accountRepository.findByUserName1(form.getUserName())
                .filter(a -> a.getUserName().equals(form.getUserName()))
                    .orElseThrow(() -> new BusinessException("Tài Khoản hoặc Mật Khẩu không đúng. Vui lòng thử lại!", ErrorCodeConstant.WRONG_ACCOUNT_OR_PASSWORD));

        if (!passwordEncoder.matches(form.getPassword(), account.getPassword()))
            throw new BusinessException("Tài Khoản hoặc Mật Khẩu không đúng. Vui lòng thử lại!", ErrorCodeConstant.WRONG_ACCOUNT_OR_PASSWORD);

        accountValidatorUtils.validatorAccountStatus(account);

        account.updateLastLogin();

        Account saved = accountRepository.save(account);
        return modelMapper.map(saved, LoginResponse.class);
    }

    @Override
    @Transactional
    public UpdateInfoAccountResponse updateInfo(Integer id, UpdateInfoAccountForm form) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id không tồn tại trong hệ thống " + id, ErrorCodeConstant.ACCOUNT_NOT_FOUND));
        accountValidatorUtils.validatorAccountStatus(account);

        accountValidator.getCurrentAccount();

        Optional<Account> existedByEmail = accountRepository.findByEmail(form.getEmail());
        if (existedByEmail.isPresent() && !id.equals(existedByEmail.get().getId())) {
            throw new BusinessException("Email đã được sử dụng vui lòng thử lại!", ErrorCodeConstant.ACCOUNT_ALREADY_EXISTS_EMAIL);
        }

        Optional<Account> existedByPhone = accountRepository.findByPhone(form.getPhone());
        if (existedByPhone.isPresent() && !id.equals(existedByPhone.get().getId())) {
            throw new BusinessException("Số điện thoại đã tồn tại trong hệ thống vui lòng thử lại!", ErrorCodeConstant.ACCOUNT_ALREADY_EXISTS_PHONE);
        }

        String currentAvatar = account.getAvatar();

        modelMapper.map(form, account);

        MultipartFile newImage = form.getAvatar();

        account.setAvatar(currentAvatar);
        if (newImage != null) {
             var b = currentAvatar != null && !currentAvatar.isBlank() && currentAvatar.startsWith("/uploads/");
            if (!newImage.isEmpty()) {
                if (b) {
                    try {
                        fileStorageService.deleteFileByUrl(currentAvatar);
                    } catch (Exception e) {
                        log.error("Lỗi khi xóa ảnh cũ: {}", currentAvatar, e);
                    }
                }

                String avatarUrl = fileStorageService.storeImage(newImage, "users");
                if (avatarUrl == null) {
                    log.error("Không thể lưu ảnh mới cho tài khoản: {}", account.getId());
                    throw new RuntimeException("Không thể lưu ảnh mới");
                }
                account.setAvatar(avatarUrl);
                log.info("Đã cập nhật avatar mới cho tài khoản: {}", account.getId());

            } else {
                // Case 2: Truyền field avatar nhưng rỗng -> giữ avatar hiện tại
                if (b) {
                    try {
                        fileStorageService.deleteFileByUrl(currentAvatar);
                        account.setAvatar(currentAvatar); // Xóa avatar
                        log.info("Đã xóa avatar cho tài khoản: {}", account.getId());
                    } catch (Exception e) {
                        log.error("Lỗi khi xóa ảnh cũ: {}", currentAvatar, e);
                    }
                } else {
                    account.setAvatar(currentAvatar); // Set ảnh cũ nếu không truyền avatar
                }
            }
        }

        Account saved = accountRepository.save(account);
        return modelMapper.map(saved, UpdateInfoAccountResponse.class);
    }

    @Override
    public List<String> getAllAccountRole() {
        return Arrays.stream(AccountRole.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllAccountGender() {
        return Arrays.stream(AccountGender.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllAccountStatus() {
        return Arrays.stream(AccountStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordForm form) {
        Account account = accountRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new NotFoundException("Email chưa được sử dụng trong hệ thống", ErrorCodeConstant.ACCOUNT_NOT_FOUND));

        accountValidatorUtils.validatorAccountStatus(account);

        String otp = String.format("%06d", new Random().nextInt(1_000_000));

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);

        account.setOtpSku(otp);
        account.setExpiryOtp(expiryTime);

        accountRepository.save(account);

        emailService.sendOtpEmailHtml(account.getEmail(), otp);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordForm form) {
        Account account = accountRepository.findByOtpSku(form.getOtpSku())
                .orElseThrow(() -> new NotFoundException("otp không hợp lệ", ErrorCodeConstant.INVALID_ACCOUNT_OTP));

        if (account.getExpiryOtp() == null || account.getExpiryOtp().isBefore(LocalDateTime.now()))
            throw new BusinessException("otp đã hết hạn", ErrorCodeConstant.OTP_HAS_EXPIRED);

        var plainTextPassword = form.getNewPassword();
        form.setNewPassword(passwordEncoder.encode(plainTextPassword));

        accountValidatorUtils.validatorAccountStatus(account);

        account.setPassword(form.getNewPassword());
        account.setOtpSku(null);
        account.setExpiryOtp(null);

        accountRepository.save(account);

        emailService.sendPasswordChangedConfirmationHtml(account.getEmail());
    }


    @Override
    @Transactional
    public void updateRole(Integer id, UpdateAccountRole form) {
        Account existedById = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id không tồn tại trong hệ thống", ErrorCodeConstant.ACCOUNT_NOT_FOUND));
        accountValidatorUtils.validatorAccountStatus(existedById);
        accountValidatorUtils.validateAccountRole(String.valueOf(existedById.getRole()));

        existedById.setRole(AccountRole.valueOf(form.getRole().toUpperCase()));
        accountRepository.save(existedById);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, UpdateAccountStatus form) {
        Account existedById = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id không tồn tại trong hệ thống", ErrorCodeConstant.ACCOUNT_NOT_FOUND));

        existedById.setStatus(AccountStatus.valueOf(form.getStatus().toUpperCase()));
        accountRepository.save(existedById);
    }

    @Override
    public AccountResponse getById(Integer id) {

        Account existedById = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id không tồn tại trong hệ thống", ErrorCodeConstant.ACCOUNT_NOT_FOUND));
        return modelMapper.map(existedById, AccountResponse.class);
    }

    @Override
    @Transactional
    public void changePassword(ChangePassword form) {
        Account account = accountValidator.getCurrentAccount();

        accountValidatorUtils.validatorAccountStatus(account);

        if(!passwordEncoder.matches(form.getCurrentPassword(), account.getPassword())) {
            throw new BusinessException("Mật khẩu hiện tại không đúng vui lòng thử lại", ErrorCodeConstant.PASSWORD_IS_NOT_RELIGIOUS);
        }

        if(passwordEncoder.matches(form.getNewPassword(), account.getPassword())) {
            throw new BusinessException("Mật khẩu mới phải khác mật cũ", ErrorCodeConstant.NEW_PASSWORD_MUST_BE_DIFFERENT_FROM_THE_OLD_HONEY);
        }

        String encodeNewPassword = passwordEncoder.encode(form.getNewPassword());

        account.setPassword(encodeNewPassword);

        accountRepository.save(account);
    }
}
