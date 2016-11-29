package com.scchuangtou.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

public class MyMutiPart {
	private Part part;
	// private MultipartFile mMultipartFile;

	public MyMutiPart(Part part) {
		this.part = part;
	}

	// public MyMutiPart(MultipartFile mMultipartFile) {
	// this.mMultipartFile = mMultipartFile;
	// }

	public void save(File file) throws IOException {
		if (part != null) {
			part.write(file.getPath());
		}
		// else if (mMultipartFile != null) {
		// mMultipartFile.transferTo(file);
		// }

	}

	public InputStream getInputStream() throws IOException {
		if (part != null) {
			return part.getInputStream();
		}
		// if (mMultipartFile != null) {
		// return mMultipartFile.getInputStream();
		// }
		throw new IOException();
	}

	public String getContentType() {
		if (part != null) {
			return part.getContentType();
		}
		// if (mMultipartFile != null) {
		// return mMultipartFile.getContentType();
		// }
		return null;
	}

	public String getName() {
		if (part != null) {
			return part.getName();
		}
		// if (mMultipartFile != null) {
		// return mMultipartFile.getName();
		// }
		return null;
	}

	public long getSize() {
		if (part != null) {
			return part.getSize();
		}
		// if (mMultipartFile != null) {
		// return mMultipartFile.getSize();
		// }
		return -1;
	}
}
