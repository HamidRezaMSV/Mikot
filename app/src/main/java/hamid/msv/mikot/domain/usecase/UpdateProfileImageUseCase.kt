package hamid.msv.mikot.domain.usecase

import android.net.Uri
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun execute(uri: Uri, currentUserId: String) = userRepository.updateProfileImage(uri, currentUserId)

}